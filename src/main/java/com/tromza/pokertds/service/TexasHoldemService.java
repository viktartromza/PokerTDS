package com.tromza.pokertds.service;

import com.tromza.pokertds.annotation.GetTimeAnnotation;
import com.tromza.pokertds.domain.BetPoker;
import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.TexasHoldemGame;
import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.domain.Wallet;
import com.tromza.pokertds.domain.enums.BetPokerType;
import com.tromza.pokertds.domain.enums.GameStatus;
import com.tromza.pokertds.domain.enums.GameType;
import com.tromza.pokertds.domain.enums.Winner;
import com.tromza.pokertds.pokerLogic.Chanses;
import com.tromza.pokertds.pokerLogic.PokerGame;
import com.tromza.pokertds.repository.GameRepository;
import com.tromza.pokertds.repository.PokerBetRepository;
import com.tromza.pokertds.repository.TexasHoldemRepository;
import com.tromza.pokertds.repository.UserRepository;
import com.tromza.pokertds.request.UserMoneyAmount;
import com.tromza.pokertds.response.TexasHoldemGameWithBetPoker;
import com.tromza.pokertds.service.impl.UserServiceImpl;
import com.tromza.pokertds.service.impl.WalletServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TexasHoldemService {
    @Value("${blind}")
    private Double BLIND;
    Logger log = LoggerFactory.getLogger(this.getClass());
    private final WalletServiceImpl walletService;
    private final GameService gameService;
    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;
    private final TexasHoldemRepository texasHoldemRepository;
    private final GameRepository gameRepository;
    private final PokerBetRepository pokerBetRepository;
    private final EmailService emailService;

    @Autowired
    public TexasHoldemService(WalletServiceImpl walletService, GameService gameService, UserServiceImpl userServiceImpl, UserRepository userRepository, TexasHoldemRepository texasHoldemRepository, GameRepository gameRepository, PokerBetRepository pokerBetRepository, EmailService emailService) {
        this.walletService = walletService;
        this.gameService = gameService;
        this.userServiceImpl = userServiceImpl;
        this.userRepository = userRepository;
        this.texasHoldemRepository = texasHoldemRepository;
        this.gameRepository = gameRepository;
        this.pokerBetRepository = pokerBetRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Optional<TexasHoldemGame> createTexasHoldemGameForUser(Principal principal) {
        User user = userServiceImpl.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login not found!"));
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
                userServiceImpl.addGameToUser(user, game);
                walletService.updateWallet(new UserMoneyAmount(user.getId(), BigDecimal.valueOf(BLIND).negate()));
                TexasHoldemGame texasHoldemGame = new TexasHoldemGame();
                texasHoldemGame.setGameId(game.getId());
                texasHoldemGame.setStatus(GameStatus.IN_PROCESS);
                texasHoldemGame.setPlayerDeposit(BLIND);
                texasHoldemGame.setBank(2 * BLIND);
                ArrayList<String> preflops = PokerGame.getPreflops();
                texasHoldemGame.setPlayerPreflop(preflops.get(0));
                texasHoldemGame.setCasinoPreflop(preflops.get(1));
                texasHoldemGame.setChanged(new Timestamp(System.currentTimeMillis()));
                return Optional.of(texasHoldemRepository.save(texasHoldemGame));
            }
        }
    }

    @Transactional
    @GetTimeAnnotation
    public TexasHoldemGameWithBetPoker playingTexasHoldem(BetPoker bet, Principal principal) throws InterruptedException {
        Game game = gameService.findTexasHoldemGameInProcess(userServiceImpl.getUserByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User with login " + principal.getName() + " not found!")).getId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        User user = userServiceImpl.getUserByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User with login " + principal.getName() + " not found!"));
        if (game.getId() != bet.getGameId()) {
            throw new UnsupportedOperationException("User with login " + principal.getName() + " hasn't game with id " + bet.getGameId() + " in process");
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
                return new TexasHoldemGameWithBetPoker(finishTexasHoldemGame(texasHoldemGame, principal), bet, texasHoldemGame.getCasinoPreflop());
            } else {
                Wallet wallet = walletService.getWalletForUser(principal).get();
                if (wallet.getBalance().doubleValue() - bet.getPlayerAmount().doubleValue() < 0) {
                    throw new UnsupportedOperationException("Bet amount can't be more than " + wallet.getBalance().doubleValue() + " $.");
                } else {
                    walletService.updateWallet(new UserMoneyAmount(user.getId(), bet.getPlayerAmount().negate()));
                    return playTexasHoldem(texasHoldemGame, bet, principal);
                }
            }
        }
    }

    @Transactional
    public TexasHoldemGame finishTexasHoldemGame(TexasHoldemGame texasHoldemGame, Principal principal) {
        User user = userServiceImpl.getUserByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User with login " + principal.getName() + " not found!"));
        Game game = gameService.findTexasHoldemGameInProcess(user.getId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        if (game.getId() != texasHoldemGame.getGameId()) {
            throw new UnsupportedOperationException("User with login " + principal.getName() + " hasn't game with id " + texasHoldemGame.getGameId() + " in process");
        } else {
            texasHoldemGame.setStatus(GameStatus.COMPLETED);
            if (texasHoldemGame.getWinner().equals(Winner.PLAYER)) {
                texasHoldemGame.setResult(texasHoldemGame.getBank() - texasHoldemGame.getPlayerDeposit());
                walletService.updateWallet(new UserMoneyAmount(user.getId(), BigDecimal.valueOf(texasHoldemGame.getBank())));

            } else if (texasHoldemGame.getWinner().equals(Winner.DRAW)) {
                texasHoldemGame.setResult(0.00);
                walletService.updateWallet(new UserMoneyAmount(user.getId(), BigDecimal.valueOf(texasHoldemGame.getPlayerDeposit())));
            } else {
                texasHoldemGame.setResult(-texasHoldemGame.getPlayerDeposit());
            }
            game.setResult(texasHoldemGame.getResult());
            user.setScore(user.getScore() + texasHoldemGame.getResult());
            gameService.finishGame(game);
            userServiceImpl.saveUser(user);
            return updateTexasHoldemGame(texasHoldemGame);
        }
    }

    public TexasHoldemGame updateTexasHoldemGame(TexasHoldemGame texasHoldemGame) {
        texasHoldemGame.setChanged(new Timestamp(System.currentTimeMillis()));
        return texasHoldemRepository.saveAndFlush(texasHoldemGame);
    }

    @Transactional
    public TexasHoldemGameWithBetPoker playTexasHoldem(TexasHoldemGame texasHoldemGame, BetPoker bet, Principal principal) throws InterruptedException {
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
                    return new TexasHoldemGameWithBetPoker(finishTexasHoldemGame(texasHoldemGame, principal), saveBetPoker(bet), texasHoldemGame.getCasinoPreflop(), winCombination);
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
                return new TexasHoldemGameWithBetPoker(finishTexasHoldemGame(texasHoldemGame, principal), saveBetPoker(bet), texasHoldemGame.getCasinoPreflop(), winCombination);
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
        userServiceImpl.saveUser(user);
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
