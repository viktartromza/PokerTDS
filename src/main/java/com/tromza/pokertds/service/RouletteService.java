package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.*;
import com.tromza.pokertds.repository.BetRepository;
import com.tromza.pokertds.repository.RouletteRepository;
import com.tromza.pokertds.request.RouletteWithBet;
import com.tromza.pokertds.request.UserMoneyAmount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;

@Service
public class RouletteService {
    Logger log = LoggerFactory.getLogger(this.getClass());
    BetRepository betRepository;
    RouletteRepository rouletteRepository;
    WalletService walletService;
    GameService gameService;
    UserService userService;

    @Autowired
    public RouletteService(BetRepository betRepository, RouletteRepository rouletteRepository, WalletService walletService, GameService gameService, UserService userService) {
        this.betRepository = betRepository;
        this.rouletteRepository = rouletteRepository;
        this.walletService = walletService;
        this.gameService = gameService;
        this.userService = userService;
    }

    public Optional<RouletteGame> getRouletteGameById(int id) {
        return rouletteRepository.findById(id);
    }

    public Optional<RouletteGame> getRouletteGameByGameId(int id) {
        return rouletteRepository.findRouletteGameByGameId(id);
    }

    /**
     * If user already has roulette in process, return old game.
     */
    public Optional<RouletteGame> createRouletteGameForUser(Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login not found!"));
        if (gameService.findRouletteGameInProcess(user.getId()).isPresent()) {
            log.info("User with id: " + user.getId() + " has roulette in process");
            return rouletteRepository.findRouletteGameByGameId(gameService.findRouletteGameInProcess(user.getId()).get().getId());
        } else {
            Optional<Wallet> optionalWallet = walletService.getWalletByUserId(user.getId());
            if (optionalWallet.isEmpty() || optionalWallet.get().getBalance().doubleValue() == 0) {
                log.info("User with id: " + user.getId() + " has zero-balance");//TODO кинуть ошибку
                return Optional.empty();
            } else {
                Game game = new Game();
                game.setType(GameType.ROULETTE_EU);
                game.setStatus(GameStatus.IN_PROCESS);
                game = gameService.createGame(game);
                RouletteGame rouletteGame = new RouletteGame();
                rouletteGame.setGameId(game.getId());
                rouletteGame.setStatus(GameStatus.IN_PROCESS);
                userService.addGameToUser(user, game);
                rouletteGame.setSpin(0);
                rouletteGame.setWins(0);
                rouletteGame.setLosses(0);
                rouletteGame.setChanged(new Timestamp(System.currentTimeMillis()));
                return Optional.of(rouletteRepository.save(rouletteGame));
            }
        }
    }

    public RouletteGame updateRouletteGame(RouletteGame rouletteGame) {
        return rouletteRepository.saveAndFlush(rouletteGame);
    }

    public RouletteWithBet playingRoulette(BetRoulette bet, Principal principal) throws Exception {
        Game game = gameService.findRouletteGameInProcess(userService.getUserByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User with login " + principal.getName() + " not found!")).getId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        if (game.getId() != bet.getGameId()) {
            throw new Exception("User with login " + principal.getName() + " hasn't game with id " + bet.getGameId() + " in process");
        } else {
            RouletteGame rouletteGame = getRouletteGameByGameId(bet.getGameId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
            Wallet wallet = walletService.getWalletForUser(principal).get();
            if (wallet.getBalance().doubleValue() + rouletteGame.getResult() - bet.getAmount().doubleValue() < 0) {
                throw new Exception("Bet amount can't be more than " + (wallet.getBalance().doubleValue() + rouletteGame.getResult()) + " $.");
            } else {
                saveBetRoulette(bet);
                RouletteWithBet rouletteWithBet = new RouletteWithBet(rouletteGame, bet);
                RouletteWithBet updRouletteWithBet = play(rouletteWithBet);
                if ((wallet.getBalance().doubleValue() + updRouletteWithBet.getRouletteGame().getResult()) <= 0) {
                    updRouletteWithBet.setRouletteGame(finishRouletteGame(updRouletteWithBet.getRouletteGame(), principal));
                } else {
                    updateRouletteGame(updRouletteWithBet.getRouletteGame());
                }
                updateBetRoulette(updRouletteWithBet.getBetRoulette());
                return updRouletteWithBet;
            }
        }
    }

    public BetRoulette saveBetRoulette(BetRoulette betRoulette) {
        return betRepository.save(betRoulette);
    }

    public BetRoulette updateBetRoulette(BetRoulette betRoulette) {
        return betRepository.saveAndFlush(betRoulette);
    }

    public RouletteGame finishRouletteGame(RouletteGame rouletteGame, Principal principal) throws Exception {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User with login " + principal.getName() + " not found!"));
        Game game = gameService.findRouletteGameInProcess(user.getId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        if (game.getId() != rouletteGame.getGameId()) {
            throw new Exception("User with login " + principal.getName() + " hasn't game with id " + rouletteGame.getGameId() + " in process");
        } else {
            rouletteGame.setStatus(GameStatus.COMPLETED);
            rouletteGame.setChanged(new Timestamp(System.currentTimeMillis()));
            game.setStatus(GameStatus.COMPLETED);
            game.setFinish(new Timestamp(System.currentTimeMillis()));
            game.setResult(rouletteGame.getResult());
            user.setScore(user.getScore() + rouletteGame.getResult());
            userService.saveUser(user);
            walletService.updateWallet(new UserMoneyAmount(user.getId(), BigDecimal.valueOf(rouletteGame.getResult())));
            return updateRouletteGame(rouletteGame);
        }
    }

    public RouletteGame finishRouletteGameById(int rouletteGameId, Principal principal) throws Exception {
        RouletteGame rouletteGame = rouletteRepository.findRouletteGameById(rouletteGameId).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        return finishRouletteGame(rouletteGame, principal);
    }

    @Scheduled(cron = "${interval-in-cron}")
    public void findNoPlayedRouletteGames() {
        Timestamp time = new Timestamp(System.currentTimeMillis() - 3600000);
        ArrayList<RouletteGame> rouletteGames = rouletteRepository.findAllByStatusIsInProcessAndChangedBefore(time);
        if (rouletteGames.isEmpty()) {
            log.info("There aren't roulette that not changed more than 1 hour!");
        } else {
            rouletteGames.forEach(roulette -> {log.info("RouletteGame with id" + roulette.getId() + " haven't been changed more than 1 hour!");
            log.info("email " + rouletteRepository.findEmailByGameId(roulette.getGameId()));
            });//TODO
        }
    }


    public RouletteWithBet play(RouletteWithBet rouletteWithBet) {

        Random generator = new Random();
        int rouletteNumber = generator.nextInt(37);
        rouletteWithBet.getBetRoulette().setRouletteNumber(rouletteNumber);
        rouletteWithBet.getRouletteGame().setSpin(rouletteWithBet.getRouletteGame().getSpin() + 1);
        double amount = rouletteWithBet.getBetRoulette().getAmount().doubleValue();
        double result = rouletteWithBet.getRouletteGame().getResult();
        int wins = rouletteWithBet.getRouletteGame().getWins();
        int losses = rouletteWithBet.getRouletteGame().getLosses();


        if (rouletteWithBet.getBetRoulette().getType() == BetType.NUMBER) {
            if (rouletteNumber == Integer.parseInt(rouletteWithBet.getBetRoulette().getPlayerChoice())) {
                rouletteWithBet.getBetRoulette().setWinAmount(36 * amount);
                rouletteWithBet.getRouletteGame().setWins(wins + 1);
                rouletteWithBet.getRouletteGame().setResult(result + 35 * amount);
            } else {
                rouletteWithBet.getBetRoulette().setWinAmount(0.00);
                rouletteWithBet.getRouletteGame().setLosses(losses + 1);
                rouletteWithBet.getRouletteGame().setResult(result - amount);
            }
        } else if (rouletteWithBet.getBetRoulette().getType() == BetType.EVEN) {
            if (rouletteNumber == 0 || rouletteNumber % 2 != 0) {
                rouletteWithBet.getBetRoulette().setWinAmount(0.00);
                rouletteWithBet.getRouletteGame().setLosses(losses + 1);
                rouletteWithBet.getRouletteGame().setResult(result - amount);
            } else {
                rouletteWithBet.getBetRoulette().setWinAmount(2 * amount);
                rouletteWithBet.getRouletteGame().setWins(wins + 1);
                rouletteWithBet.getRouletteGame().setResult(result + amount);
            }
        } else if (rouletteNumber % 2 == 0) {
            rouletteWithBet.getBetRoulette().setWinAmount(0.00);
            rouletteWithBet.getRouletteGame().setLosses(losses + 1);
            rouletteWithBet.getRouletteGame().setResult(result - amount);
        } else {
            rouletteWithBet.getBetRoulette().setWinAmount(2 * amount);
            rouletteWithBet.getRouletteGame().setWins(wins + 1);
            rouletteWithBet.getRouletteGame().setResult(result + amount);
        }
        rouletteWithBet.getRouletteGame().setChanged(new Timestamp(System.currentTimeMillis()));
        return rouletteWithBet;
    }
}
