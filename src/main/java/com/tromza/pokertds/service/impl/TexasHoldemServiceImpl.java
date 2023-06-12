package com.tromza.pokertds.service.impl;

import com.tromza.pokertds.annotation.GetTimeAnnotation;
import com.tromza.pokertds.model.domain.BetPoker;
import com.tromza.pokertds.model.domain.Game;
import com.tromza.pokertds.model.domain.TexasHoldemGame;
import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.model.domain.Wallet;
import com.tromza.pokertds.model.enums.BetPokerType;
import com.tromza.pokertds.model.enums.GameStatus;
import com.tromza.pokertds.model.enums.GameType;
import com.tromza.pokertds.model.enums.Winner;
import com.tromza.pokertds.gamesLogic.pokerLogic.Chanses;
import com.tromza.pokertds.gamesLogic.pokerLogic.PokerGame;
import com.tromza.pokertds.repository.GameRepository;
import com.tromza.pokertds.repository.PokerBetRepository;
import com.tromza.pokertds.repository.TexasHoldemRepository;
import com.tromza.pokertds.repository.UserRepository;
import com.tromza.pokertds.model.pairs.TexasHoldemGameWithBetPoker;
import com.tromza.pokertds.service.EmailService;
import com.tromza.pokertds.service.GameService;
import com.tromza.pokertds.service.TexasHoldemService;
import com.tromza.pokertds.service.UserService;
import com.tromza.pokertds.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TexasHoldemServiceImpl implements TexasHoldemService {
    @Value("${blind}")
    private Double BLIND;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final WalletService walletService;
    private final GameService gameService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TexasHoldemRepository texasHoldemRepository;
    private final GameRepository gameRepository;
    private final PokerBetRepository pokerBetRepository;
    private final EmailService emailService;

    public TexasHoldemServiceImpl(WalletService walletService, GameService gameService, UserService userService, UserRepository userRepository, TexasHoldemRepository texasHoldemRepository, GameRepository gameRepository, PokerBetRepository pokerBetRepository, EmailService emailService) {
        this.walletService = walletService;
        this.gameService = gameService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.texasHoldemRepository = texasHoldemRepository;
        this.gameRepository = gameRepository;
        this.pokerBetRepository = pokerBetRepository;
        this.emailService = emailService;
    }

    public Optional<TexasHoldemGame> getTexasHoldemGameByGameId(int id) {
        return texasHoldemRepository.findTexasHoldemGameByGameId(id);
    }

    @Transactional
    public TexasHoldemGame createTexasHoldemGameForUser(User user) {
        if (gameService.findTexasHoldemGameInProcess(user.getId()).isPresent()) {
            log.info("User with id: " + user.getId() + " has texas holdem in process");
            return texasHoldemRepository.findTexasHoldemGameByGameId(gameService.findTexasHoldemGameInProcess(user.getId()).get().getId()).orElseThrow(() -> new NoSuchElementException());
        } else {
            Optional<Wallet> optionalWallet = walletService.getWalletByUserId(user.getId());
            if (optionalWallet.isEmpty() || optionalWallet.get().getBalance().doubleValue() < BLIND) {
                throw new UnsupportedOperationException("User with id: " + user.getId() + " hasn't enough money");
            } else {
                Wallet wallet = optionalWallet.get();
                Game game = new Game();
                game.setType(GameType.TEXAS_HOLDEM);
                game = gameService.createGame(game);
                userService.addGameToUser(user, game);
                walletService.updateWallet(wallet, BigDecimal.valueOf(BLIND).negate());
                TexasHoldemGame texasHoldemGame = new TexasHoldemGame();
                texasHoldemGame.setGameId(game.getId());
                texasHoldemGame.setStatus(GameStatus.IN_PROCESS);
                texasHoldemGame.setPlayerDeposit(BLIND);
                texasHoldemGame.setBank(2 * BLIND);
                ArrayList<String> preflops = PokerGame.getPreflops();
                texasHoldemGame.setPlayerPreflop(preflops.get(0));
                texasHoldemGame.setCasinoPreflop(preflops.get(1));
                texasHoldemGame.setChanged(new Timestamp(System.currentTimeMillis()));
                return texasHoldemRepository.save(texasHoldemGame);
            }
        }
    }

    @Transactional
    @GetTimeAnnotation
    public TexasHoldemGameWithBetPoker playingTexasHoldem(BetPoker bet, User user) throws InterruptedException {
        Game game = gameService.findTexasHoldemGameInProcess(user.getId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        if (game.getId() != bet.getGameId()) {
            throw new UnsupportedOperationException("User with login " + user.getLogin() + " hasn't game with id " + bet.getGameId() + " in process");
        } else {
            TexasHoldemGame texasHoldemGame = texasHoldemRepository.findTexasHoldemGameByGameId(bet.getGameId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
            if (bet.getTypePlayer() == BetPokerType.FOLD) {
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
                texasHoldemGame.setWinner(Winner.CASINO);
                return new TexasHoldemGameWithBetPoker(finishTexasHoldemGame(texasHoldemGame, user), bet, texasHoldemGame.getCasinoPreflop());
            } else {
                Wallet wallet = walletService.getWalletForUser(user).get();
                if (wallet.getBalance().doubleValue() - bet.getPlayerAmount().doubleValue() < 0) {
                    throw new UnsupportedOperationException("Bet amount can't be more than " + wallet.getBalance().doubleValue() + " $.");
                } else {
                    walletService.updateWallet(wallet, bet.getPlayerAmount().negate());
                    return playTexasHoldem(texasHoldemGame, bet, user);
                }
            }
        }
    }

    @Transactional
    public TexasHoldemGame finishTexasHoldemGame(TexasHoldemGame texasHoldemGame, User user) {
        Game game = gameService.findTexasHoldemGameInProcess(user.getId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        Wallet wallet = walletService.getWalletForUser(user).get();
        if (game.getId() != texasHoldemGame.getGameId()) {
            throw new UnsupportedOperationException("User with login " + user.getLogin() + " hasn't game with id " + texasHoldemGame.getGameId() + " in process");
        } else {
            texasHoldemGame.setStatus(GameStatus.COMPLETED);
            if (texasHoldemGame.getWinner().equals(Winner.PLAYER)) {
                texasHoldemGame.setResult(texasHoldemGame.getBank() - texasHoldemGame.getPlayerDeposit());
                walletService.updateWallet(wallet, BigDecimal.valueOf(texasHoldemGame.getBank()));
            } else if (texasHoldemGame.getWinner().equals(Winner.DRAW)) {
                texasHoldemGame.setResult(0.00);
                walletService.updateWallet(wallet, BigDecimal.valueOf(texasHoldemGame.getPlayerDeposit()));
            } else {
                texasHoldemGame.setResult(-texasHoldemGame.getPlayerDeposit());
            }
            game.setResult(texasHoldemGame.getResult());
            user.setScore(user.getScore() + texasHoldemGame.getResult());
            gameService.finishGame(game);
            userService.saveUser(user);
            return updateTexasHoldemGame(texasHoldemGame);
        }
    }

    public TexasHoldemGame updateTexasHoldemGame(TexasHoldemGame texasHoldemGame) {
        texasHoldemGame.setChanged(new Timestamp(System.currentTimeMillis()));
        return texasHoldemRepository.saveAndFlush(texasHoldemGame);
    }

    @Transactional
    public TexasHoldemGameWithBetPoker playTexasHoldem(TexasHoldemGame texasHoldemGame, BetPoker bet, User user) throws InterruptedException {
        Pattern card = Pattern.compile("[2-9,TJQKA][hcds]");
        Matcher ourHandMatcher = card.matcher(texasHoldemGame.getCasinoPreflop());
        String[] ourHand = new String[2];
        int i = 0;
        while (ourHandMatcher.find()) {
            ourHand[i] = ourHandMatcher.group();
            i++;
        }
        Matcher playerHandMatcher = card.matcher(texasHoldemGame.getPlayerPreflop());
        String[] playerHand = new String[2];
        i = 0;
        while (playerHandMatcher.find()) {
            playerHand[i] = playerHandMatcher.group();
            i++;
        }
        texasHoldemGame.setPlayerDeposit(texasHoldemGame.getPlayerDeposit() + bet.getPlayerAmount().doubleValue());
        if (texasHoldemGame.getFlop() == null) {
            bet.setRound(1);
            if (bet.getTypePlayer().equals(BetPokerType.BET)) {
                AtomicReference<Double> chansesCasino = new AtomicReference<>((double) 0);
                AtomicReference<Double> chansesPlayer = new AtomicReference<>((double) 0);
                var threadFirst = new Thread(() -> chansesCasino.set(Chanses.compCombinations(ourHand, null)));
                var threadSecond = new Thread(() -> {
                    try {
                        chansesPlayer.set(Chanses.compCombinationsPlayer(ourHand, null));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                threadFirst.start();
                threadFirst.join();
                threadSecond.start();
                threadSecond.join();
                if (chansesCasino.get() > chansesPlayer.get()) {
                    bet.setTypeCasino(BetPokerType.RISE);
                    bet.setCasinoAmount(bet.getPlayerAmount().add(BigDecimal.valueOf(BLIND)));
                } else {
                    bet.setTypeCasino(BetPokerType.CALL);
                    bet.setCasinoAmount(bet.getPlayerAmount());
                    texasHoldemGame.setFlop(PokerGame.getFlop(ourHand, playerHand));
                }
                texasHoldemGame.setBank(texasHoldemGame.getBank() + bet.getPlayerAmount().doubleValue() + bet.getCasinoAmount().doubleValue());
                return new TexasHoldemGameWithBetPoker(updateTexasHoldemGame(texasHoldemGame), saveBetPoker(bet));
            }
            if (bet.getTypePlayer().equals(BetPokerType.CALL)) {
                texasHoldemGame.setBank(texasHoldemGame.getBank() + bet.getPlayerAmount().doubleValue());
                texasHoldemGame.setFlop(PokerGame.getFlop(ourHand, playerHand));
                return new TexasHoldemGameWithBetPoker(updateTexasHoldemGame(texasHoldemGame), saveBetPoker(bet));
            }
        } else if (texasHoldemGame.getTern() == null) {
            bet.setRound(2);
            Matcher boardMatcher = card.matcher(texasHoldemGame.getFlop());
            String[] board = new String[3];
            i = 0;
            while (boardMatcher.find()) {
                board[i] = boardMatcher.group();
                i++;
            }
            if (bet.getTypePlayer().equals(BetPokerType.BET)) {
                AtomicReference<Double> chansesCasino = new AtomicReference<>((double) 0);
                AtomicReference<Double> chansesPlayer = new AtomicReference<>((double) 0);
                var threadFirst = new Thread(() -> chansesCasino.set(Chanses.compCombinations(ourHand, board)));
                var threadSecond = new Thread(() -> {
                    try {
                        chansesPlayer.set(Chanses.compCombinationsPlayer(ourHand, board));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                threadFirst.start();
                threadFirst.join();
                threadSecond.start();
                threadSecond.join();
                if (chansesCasino.get() > chansesPlayer.get()) {
                    bet.setTypeCasino(BetPokerType.RISE);
                    bet.setCasinoAmount(bet.getPlayerAmount().add(BigDecimal.valueOf(BLIND)));
                } else {
                    bet.setTypeCasino(BetPokerType.CALL);
                    bet.setCasinoAmount(bet.getPlayerAmount());
                    texasHoldemGame.setTern(PokerGame.getTern(ourHand, playerHand, board));
                }
                texasHoldemGame.setBank(texasHoldemGame.getBank() + bet.getPlayerAmount().doubleValue() + bet.getCasinoAmount().doubleValue());
                return new TexasHoldemGameWithBetPoker(updateTexasHoldemGame(texasHoldemGame), saveBetPoker(bet));
            }
            if (bet.getTypePlayer().equals(BetPokerType.CALL)) {
                texasHoldemGame.setBank(texasHoldemGame.getBank() + bet.getPlayerAmount().doubleValue());
                texasHoldemGame.setTern(PokerGame.getTern(ourHand, playerHand, board));
                return new TexasHoldemGameWithBetPoker(updateTexasHoldemGame(texasHoldemGame), saveBetPoker(bet));
            }
        } else if (texasHoldemGame.getRiver() == null) {
            bet.setRound(3);
            Matcher boardMatcher = card.matcher(texasHoldemGame.getFlop());
            String[] board = new String[4];
            i = 0;
            while (boardMatcher.find()) {
                board[i] = boardMatcher.group();
                i++;
            }
            board[3] = texasHoldemGame.getTern();
            if (bet.getTypePlayer().equals(BetPokerType.BET)) {
                AtomicReference<Double> chansesCasino = new AtomicReference<>((double) 0);
                AtomicReference<Double> chansesPlayer = new AtomicReference<>((double) 0);
                var threadFirst = new Thread(() -> chansesCasino.set(Chanses.compCombinations(ourHand, board)));
                var threadSecond = new Thread(() -> {
                    try {
                        chansesPlayer.set(Chanses.compCombinationsPlayer(ourHand, board));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                threadFirst.start();
                threadFirst.join();
                threadSecond.start();
                threadSecond.join();
                if (chansesCasino.get() > chansesPlayer.get()) {
                    bet.setTypeCasino(BetPokerType.RISE);
                    bet.setCasinoAmount(bet.getPlayerAmount().add(BigDecimal.valueOf(BLIND)));
                } else {
                    bet.setTypeCasino(BetPokerType.CALL);
                    bet.setCasinoAmount(bet.getPlayerAmount());
                    texasHoldemGame.setRiver(PokerGame.getRiver(ourHand, playerHand, board));
                }
                texasHoldemGame.setBank(texasHoldemGame.getBank() + bet.getPlayerAmount().doubleValue() + bet.getCasinoAmount().doubleValue());
                return new TexasHoldemGameWithBetPoker(updateTexasHoldemGame(texasHoldemGame), saveBetPoker(bet));
            }
            if (bet.getTypePlayer().equals(BetPokerType.CALL)) {
                texasHoldemGame.setBank(texasHoldemGame.getBank() + bet.getPlayerAmount().doubleValue());
                texasHoldemGame.setRiver(PokerGame.getRiver(ourHand, playerHand, board));
                return new TexasHoldemGameWithBetPoker(updateTexasHoldemGame(texasHoldemGame), saveBetPoker(bet));
            }
        } else {
            bet.setRound(4);
            Matcher boardMatcher = card.matcher(texasHoldemGame.getFlop());
            String[] board = new String[5];
            i = 0;
            while (boardMatcher.find()) {
                board[i] = boardMatcher.group();
                i++;
            }
            board[3] = texasHoldemGame.getTern();
            board[4] = texasHoldemGame.getRiver();
            Map.Entry<String, Double> casinoResult = Chanses.evalCombinationByHandAndBoard(ourHand, board);
            Map.Entry<String, Double> playerResult = Chanses.evalCombinationByHandAndBoard(playerHand, board);
            double resultCasino = casinoResult.getValue();
            String bestCombCasino = casinoResult.getKey();
            double resultBoard = Chanses.evalCombination(board);
            double chansesPlayer = Chanses.compCombinationsPlayer(ourHand, board);
            double resultPlayer = playerResult.getValue();
            String bestCombPlayer = playerResult.getKey();
            if (bet.getTypePlayer().equals(BetPokerType.BET)) {
                if (resultCasino > 3 && chansesPlayer < 2.5 && resultCasino > resultBoard) {
                    bet.setTypeCasino(BetPokerType.RISE);
                    bet.setCasinoAmount(bet.getPlayerAmount().add(BigDecimal.valueOf(BLIND)));
                    texasHoldemGame.setBank(texasHoldemGame.getBank() + bet.getPlayerAmount().doubleValue() + bet.getCasinoAmount().doubleValue());
                    return new TexasHoldemGameWithBetPoker(updateTexasHoldemGame(texasHoldemGame), saveBetPoker(bet));
                } else {
                    String winCombination;
                    bet.setTypeCasino(BetPokerType.CALL);
                    bet.setCasinoAmount(bet.getPlayerAmount());
                    texasHoldemGame.setBank(texasHoldemGame.getBank() + bet.getPlayerAmount().doubleValue() + bet.getCasinoAmount().doubleValue());
                    if (resultPlayer > resultCasino) {
                        texasHoldemGame.setWinner(Winner.PLAYER);
                        winCombination = bestCombPlayer;
                    } else if (resultPlayer < resultCasino) {
                        texasHoldemGame.setWinner(Winner.CASINO);
                        winCombination = bestCombCasino;
                    } else {
                        texasHoldemGame.setWinner(Winner.DRAW);
                        winCombination = bestCombPlayer;
                    }
                    return new TexasHoldemGameWithBetPoker(finishTexasHoldemGame(texasHoldemGame, user), saveBetPoker(bet), texasHoldemGame.getCasinoPreflop(), winCombination);
                }
            }
            if (bet.getTypePlayer().equals(BetPokerType.CALL)) {
                texasHoldemGame.setBank(texasHoldemGame.getBank() + bet.getPlayerAmount().doubleValue());
                String winCombination;
                if (resultPlayer > resultCasino) {
                    texasHoldemGame.setWinner(Winner.PLAYER);
                    winCombination = bestCombPlayer;
                } else if (resultPlayer < resultCasino) {
                    texasHoldemGame.setWinner(Winner.CASINO);
                    winCombination = bestCombCasino;
                } else {
                    texasHoldemGame.setWinner(Winner.DRAW);
                    winCombination = bestCombPlayer;
                }
                return new TexasHoldemGameWithBetPoker(finishTexasHoldemGame(texasHoldemGame, user), saveBetPoker(bet), texasHoldemGame.getCasinoPreflop(), winCombination);
            }
        }
        throw new UnsupportedOperationException("Something wrong");
    }

    public BetPoker saveBetPoker(BetPoker bet) {
        return pokerBetRepository.saveAndFlush(bet);
    }

    @Scheduled(fixedRate = 300000)//каждые 5 мин
    public void findNoPlayedTexasHoldemGames() {
        Timestamp time = new Timestamp(System.currentTimeMillis() - 300000);//не менялись 5 минут
        ArrayList<TexasHoldemGame> texasHoldemGames = texasHoldemRepository.findAllByStatusIsInProcessAndChangedBefore(time);
        if (texasHoldemGames.isEmpty()) {
            log.info("There aren't texasHoldem-games that not changed more than 5 minutes");
        } else {
            texasHoldemGames.forEach(game -> new Thread(() -> {
                log.info("TexasHoldemGame with id " + game.getId() + " haven't been changed more than 5 minutes!");
                String email = gameRepository.findEmailByGameId(game.getGameId());
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(email);
                mailMessage.setSubject("Your game will be finished automatically");
                mailMessage.setText("Dear player, your texasHoldem-game will be finished automatically");
                emailService.sendEmail(mailMessage);
                try {
                    Thread.sleep(180000);
                } catch (InterruptedException e) {
                    log.warn("We have InterruptedException in findNoPlayedTexasHoldemGames");
                    throw new RuntimeException(e);
                }
                finishNoPlayedTexasHoldemGames(game);
            }).start());
        }
    }

    @Transactional
    public void finishNoPlayedTexasHoldemGames(TexasHoldemGame texasHoldemGame) {
        Game game = gameService.getGameById(texasHoldemGame.getGameId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        User user = userRepository.findUserIdByGameId(texasHoldemGame.getGameId()).map(userId -> userRepository.findById(userId)).orElseThrow(() -> new NoSuchElementException("User not found")).orElseThrow(() -> new NoSuchElementException("User not found"));
        texasHoldemGame.setStatus(GameStatus.COMPLETED);
        texasHoldemGame.setWinner(Winner.CASINO);
        texasHoldemGame.setResult(-texasHoldemGame.getPlayerDeposit());
        game.setResult(texasHoldemGame.getResult());
        user.setScore(user.getScore() + texasHoldemGame.getResult());
        gameService.finishGame(game);
        userService.saveUser(user);
        updateTexasHoldemGame(texasHoldemGame);
        log.info("TexasHoldem-game " + texasHoldemGame.getId() + " was finished automatically");
        String email = gameRepository.findEmailByGameId(texasHoldemGame.getGameId());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Your game was finished automatically");
        mailMessage.setText("Dear player, your texasHoldem-game was finished automatically");
        emailService.sendEmail(mailMessage);
    }
}
