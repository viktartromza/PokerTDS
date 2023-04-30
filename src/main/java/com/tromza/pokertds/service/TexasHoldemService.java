package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.*;
import com.tromza.pokertds.pokerLogic.Chanses;
import com.tromza.pokertds.pokerLogic.PokerGame;
import com.tromza.pokertds.repository.PokerBetRepository;
import com.tromza.pokertds.repository.TexasHoldemRepository;
import com.tromza.pokertds.request.UserMoneyAmount;
import com.tromza.pokertds.response.TexasHoldemGameWithBetPoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TexasHoldemService {
    @Value("${blind}")
    private Double BLIND;
    Logger log = LoggerFactory.getLogger(this.getClass());
    private final WalletService walletService;
    private final GameService gameService;
    private final UserService userService;
    private final TexasHoldemRepository texasHoldemRepository;
    private final PokerBetRepository pokerBetRepository;
    private final EmailService emailService;

    @Autowired
    public TexasHoldemService(WalletService walletService, GameService gameService, UserService userService, TexasHoldemRepository texasHoldemRepository, PokerBetRepository pokerBetRepository, EmailService emailService) {
        this.walletService = walletService;
        this.gameService = gameService;
        this.userService = userService;
        this.texasHoldemRepository = texasHoldemRepository;
        this.pokerBetRepository = pokerBetRepository;
        this.emailService = emailService;
    }

    public Optional<TexasHoldemGame> createTexasHoldemGameForUser(Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login not found!"));
        if (gameService.findTexasHoldemGameInProcess(user.getId()).isPresent()) {
            log.info("User with id: " + user.getId() + " has texas holdem in process");
            return texasHoldemRepository.findTexasHoldemGameByGameId(gameService.findTexasHoldemGameInProcess(user.getId()).get().getId());
        } else {
            Optional<Wallet> optionalWallet = walletService.getWalletByUserId(user.getId());
            if (optionalWallet.isEmpty() || optionalWallet.get().getBalance().doubleValue() < BLIND) {
                throw new UnsupportedOperationException("User with id: " + user.getId() + " hasn't enough money");
            } else {
                Game game = new Game();
                game.setType(GameType.TEXAS_HOLDEM);
                game = gameService.createGame(game);
                userService.addGameToUser(user, game);
                walletService.updateWallet(new UserMoneyAmount(user.getId(), BigDecimal.valueOf(BLIND).negate()));
                TexasHoldemGame texasHoldemGame = new TexasHoldemGame();
                texasHoldemGame.setGameId(game.getId());
                texasHoldemGame.setStatus(GameStatus.IN_PROCESS);
                texasHoldemGame.setPlayerDeposit(BLIND);
                texasHoldemGame.setBank(2 * BLIND);
                texasHoldemGame.setPlayerPreflop(PokerGame.getPreflops().get(0));
                texasHoldemGame.setCasinoPreflop(PokerGame.getPreflops().get(1));
                texasHoldemGame.setChanged(new Timestamp(System.currentTimeMillis()));
                return Optional.of(texasHoldemRepository.save(texasHoldemGame));
            }
        }
    }

    public TexasHoldemGameWithBetPoker playingTexasHoldem(BetPoker bet, Principal principal) {
        Game game = gameService.findTexasHoldemGameInProcess(userService.getUserByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User with login " + principal.getName() + " not found!")).getId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        if (game.getId() != bet.getGameId()) {
            throw new UnsupportedOperationException("User with login " + principal.getName() + " hasn't game with id " + bet.getGameId() + " in process");
        } else {
            TexasHoldemGame texasHoldemGame = texasHoldemRepository.findTexasHoldemGameByGameId(bet.getGameId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));


            Wallet wallet = walletService.getWalletForUser(principal).get();
            if (wallet.getBalance().doubleValue() - bet.getPlayerAmount().doubleValue() < 0) {
                throw new UnsupportedOperationException("Bet amount can't be more than " + wallet.getBalance().doubleValue() + " $.");
            } else if (bet.getTypePlayer() == BetPokerType.FOLD) {
                if (texasHoldemGame.getFlop() == null) {
                    bet.setRound(1);
                } else if (texasHoldemGame.getTern() == null) {
                    bet.setRound(2);
                } else if (texasHoldemGame.getRiver() == null) {
                    bet.setRound(3);
                } else {
                    bet.setRound(4);
                }
                saveBetPoker(bet);
                texasHoldemGame.setResult(-texasHoldemGame.getPlayerDeposit());
                texasHoldemGame.setWinner(Winner.CASINO);
                return new TexasHoldemGameWithBetPoker(finishTexasHoldemGame(texasHoldemGame, principal), bet);
            } else {
                return playTexasHoldem(texasHoldemGame, bet);//TODO
            }
        }
    }

    public TexasHoldemGame finishTexasHoldemGame(TexasHoldemGame texasHoldemGame, Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User with login " + principal.getName() + " not found!"));
        Game game = gameService.findTexasHoldemGameInProcess(user.getId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        if (game.getId() != texasHoldemGame.getGameId()) {
            throw new UnsupportedOperationException("User with login " + principal.getName() + " hasn't game with id " + texasHoldemGame.getGameId() + " in process");
        } else {
            texasHoldemGame.setStatus(GameStatus.COMPLETED);
            texasHoldemGame.setChanged(new Timestamp(System.currentTimeMillis()));
            game.setResult(texasHoldemGame.getResult());
            user.setScore(user.getScore() + texasHoldemGame.getResult());
            gameService.finishGame(game);
            userService.saveUser(user);
            if (texasHoldemGame.getWinner().equals(Winner.PLAYER)) {
                walletService.updateWallet(new UserMoneyAmount(user.getId(), BigDecimal.valueOf(texasHoldemGame.getBank())));
            }
            if (texasHoldemGame.getWinner().equals(Winner.DRAW)) {
                walletService.updateWallet(new UserMoneyAmount(user.getId(), BigDecimal.valueOf(texasHoldemGame.getBank()).divide(BigDecimal.valueOf(2))));
            }
            return updateTexasHoldemGame(texasHoldemGame);
        }
    }

    public TexasHoldemGame updateTexasHoldemGame(TexasHoldemGame texasHoldemGame) {
        return texasHoldemRepository.saveAndFlush(texasHoldemGame);
    }

    public TexasHoldemGameWithBetPoker playTexasHoldem(TexasHoldemGame texasHoldemGame, BetPoker bet) {
        String[] ourHand = {texasHoldemGame.getCasinoPreflop().substring(0, 2), texasHoldemGame.getCasinoPreflop().substring(2, 4)};
        String[] playerHand = {texasHoldemGame.getPlayerPreflop().substring(0, 2), texasHoldemGame.getPlayerPreflop().substring(2, 4)};
        if (texasHoldemGame.getFlop() == null) {
            bet.setRound(1);
            if (bet.getTypePlayer().equals(BetPokerType.BET)) {
                double chansesCasino = Chanses.compCombinations(ourHand, null);
                double chansesPlayer = Chanses.compCombinationsPlayer(ourHand, null);
                System.out.println("casino: " + chansesCasino + "player: " + chansesPlayer);
                if (chansesCasino > chansesPlayer) {
                    bet.setTypeCasino(BetPokerType.RISE);
                    bet.setCasinoAmount(bet.getPlayerAmount().add(BigDecimal.valueOf(BLIND)));
                    texasHoldemGame.setBank(texasHoldemGame.getBank() + bet.getCasinoAmount().doubleValue());
                    return new TexasHoldemGameWithBetPoker(updateTexasHoldemGame(texasHoldemGame), saveBetPoker(bet));
                } else {
                    bet.setTypeCasino(BetPokerType.CALL);
                    bet.setCasinoAmount(bet.getPlayerAmount());
                    texasHoldemGame.setFlop(PokerGame.getFlop(ourHand, playerHand));
                    return new TexasHoldemGameWithBetPoker(updateTexasHoldemGame(texasHoldemGame), saveBetPoker(bet));
                }
            }
            if (bet.getTypePlayer().equals(BetPokerType.CALL)) {
                texasHoldemGame.setBank(texasHoldemGame.getBank() + bet.getPlayerAmount().doubleValue());
                texasHoldemGame.setFlop(PokerGame.getFlop(ourHand, playerHand));
                return new TexasHoldemGameWithBetPoker(updateTexasHoldemGame(texasHoldemGame), saveBetPoker(bet));
            }
        }
        return null;//TODO
    }

    public BetPoker saveBetPoker(BetPoker bet) {
        return pokerBetRepository.saveAndFlush(bet);
    }
}
