package com.tromza.pokertds.service.impl;

import com.tromza.pokertds.model.domain.BetRoulette;
import com.tromza.pokertds.model.domain.Game;
import com.tromza.pokertds.model.domain.RouletteGame;
import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.model.domain.Wallet;
import com.tromza.pokertds.model.enums.BetType;
import com.tromza.pokertds.model.enums.GameStatus;
import com.tromza.pokertds.model.enums.GameType;
import com.tromza.pokertds.model.pairs.RouletteWithBet;
import com.tromza.pokertds.repository.BetRepository;
import com.tromza.pokertds.repository.GameRepository;
import com.tromza.pokertds.repository.RouletteRepository;
import com.tromza.pokertds.repository.UserRepository;
import com.tromza.pokertds.service.RouletteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;


@Service
public class RouletteServiceImpl implements RouletteService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final BetRepository betRepository;
    private final GameRepository gameRepository;
    private final RouletteRepository rouletteRepository;
    private final WalletServiceImpl walletService;
    private final GameServiceImpl gameService;
    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;

    @Autowired
    public RouletteServiceImpl(BetRepository betRepository, GameRepository gameRepository, RouletteRepository rouletteRepository, WalletServiceImpl walletService, GameServiceImpl gameService, UserServiceImpl userServiceImpl, UserRepository userRepository, EmailServiceImpl emailService) {
        this.betRepository = betRepository;
        this.gameRepository = gameRepository;
        this.rouletteRepository = rouletteRepository;
        this.walletService = walletService;
        this.gameService = gameService;
        this.userServiceImpl = userServiceImpl;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public Optional<RouletteGame> getRouletteGameById(int id) {
        return rouletteRepository.findById(id);
    }

    public Optional<RouletteGame> getRouletteGameByGameId(int id) {
        return rouletteRepository.findRouletteGameByGameId(id);
    }

    @Transactional
    public RouletteGame createRouletteGameForUser(User user) {
        if (gameService.findRouletteGameInProcess(user.getId()).isPresent()) {
            log.info("User with id: " + user.getId() + " has roulette in process");
            return rouletteRepository.findRouletteGameByGameId(gameService.findRouletteGameInProcess(user.getId()).get().getId()).orElseThrow(NoSuchElementException::new);
        } else {
            Optional<Wallet> optionalWallet = walletService.getWalletByUserId(user.getId());
            if (optionalWallet.isEmpty() || optionalWallet.get().getBalance().doubleValue() == 0) {
                throw new UnsupportedOperationException("User with id: " + user.getId() + " has zero-balance");
            } else {
                Game game = new Game();
                game.setType(GameType.ROULETTE_EU);
                game = gameService.createGame(game);
                RouletteGame rouletteGame = new RouletteGame();
                rouletteGame.setGameId(game.getId());
                rouletteGame.setStatus(GameStatus.IN_PROCESS);
                userServiceImpl.addGameToUser(user, game);
                rouletteGame.setSpin(0);
                rouletteGame.setWins(0);
                rouletteGame.setLosses(0);
                rouletteGame.setChanged(new Timestamp(System.currentTimeMillis()));
                return rouletteRepository.save(rouletteGame);
            }
        }
    }
    public RouletteWithBet play (RouletteWithBet rouletteWithBet) {
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

    public RouletteGame updateRouletteGame(RouletteGame rouletteGame) {
        return rouletteRepository.saveAndFlush(rouletteGame);
    }

    public void saveBetRoulette(BetRoulette betRoulette) {
        betRepository.save(betRoulette);
    }

    public void updateBetRoulette(BetRoulette betRoulette) {
        betRepository.saveAndFlush(betRoulette);
    }

    @Transactional
    public RouletteGame finishRouletteGame(RouletteGame rouletteGame, User user, Game game) {
        {
            rouletteGame.setStatus(GameStatus.COMPLETED);
            rouletteGame.setChanged(new Timestamp(System.currentTimeMillis()));
            game.setResult(rouletteGame.getResult());
            user.setScore(user.getScore() + rouletteGame.getResult());
            gameService.finishGame(game);
            userServiceImpl.saveUser(user);
            walletService.updateWallet(walletService.getWalletForUser(user).get(), BigDecimal.valueOf(rouletteGame.getResult()));
            return updateRouletteGame(rouletteGame);
        }
    }

    @Transactional
    public void finishRouletteGameAutomatically(RouletteGame rouletteGame) {
        Game game = gameService.getGameById(rouletteGame.getGameId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        User user = userRepository.findUserIdByGameId(rouletteGame.getGameId()).map(userId -> userRepository.findById(userId)).orElseThrow(() -> new NoSuchElementException("User not found")).orElseThrow(() -> new NoSuchElementException("User not found"));
        rouletteGame.setStatus(GameStatus.COMPLETED);
        rouletteGame.setChanged(new Timestamp(System.currentTimeMillis()));
        game.setResult(rouletteGame.getResult());
        user.setScore(user.getScore() + rouletteGame.getResult());
        gameService.finishGame(game);
        userServiceImpl.saveUser(user);
        walletService.updateWallet(walletService.getWalletForUser(user).get(), BigDecimal.valueOf(rouletteGame.getResult()));
        updateRouletteGame(rouletteGame);
        log.info("Roulette-game" + rouletteGame.getId() + "was finished automatically");
        String email = gameRepository.findEmailByGameId(rouletteGame.getGameId());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Your game was finished automatically");
        mailMessage.setText("Dear player, your roulette-game was finished automatically");
        emailService.sendEmail(mailMessage);
    }

    @Scheduled(cron = "${interval-in-cron-check}")
    public void findNoPlayedRouletteGames() {
        Timestamp time = new Timestamp(System.currentTimeMillis() - 3600000);
        List<RouletteGame> rouletteGames = rouletteRepository.findAllByStatusIsInProcessAndChangedBefore(time);
        if (rouletteGames.isEmpty()) {
            log.info("There aren't roulette that not changed more than 1 hour!");
        } else {
            rouletteGames.forEach(roulette -> {
                log.info("RouletteGame with id" + roulette.getId() + " haven't been changed more than 1 hour!");
                String email = gameRepository.findEmailByGameId(roulette.getGameId());
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(email);
                mailMessage.setSubject("Your game will be finished automatically");
                mailMessage.setText("Dear player, your roulette-game will be finished automatically");
                emailService.sendEmail(mailMessage);
            });
        }
    }

    @Transactional
    @Scheduled(cron = "${interval-in-cron-finish}")
    public void finishNoPlayedRouletteGames() {
        Timestamp time = new Timestamp(System.currentTimeMillis() - 5400000);
        List<RouletteGame> rouletteGames = rouletteRepository.findAllByStatusIsInProcessAndChangedBefore(time);
        if (rouletteGames.isEmpty()) {
            log.info("There aren't roulette that not changed more than 1,5 hour!");
        } else {
            rouletteGames.forEach(this::finishRouletteGameAutomatically);
        }
    }
}
