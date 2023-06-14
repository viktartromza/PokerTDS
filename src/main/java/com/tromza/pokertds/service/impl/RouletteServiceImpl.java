package com.tromza.pokertds.service.impl;

import com.tromza.pokertds.model.domain.BetRoulette;
import com.tromza.pokertds.model.domain.Game;
import com.tromza.pokertds.model.domain.RouletteGame;
import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.model.domain.Wallet;
import com.tromza.pokertds.model.enums.BetType;
import com.tromza.pokertds.model.enums.GameStatus;
import com.tromza.pokertds.model.enums.GameType;
import com.tromza.pokertds.repository.RouletteBetRepository;
import com.tromza.pokertds.repository.GameRepository;
import com.tromza.pokertds.repository.RouletteRepository;
import com.tromza.pokertds.repository.UserRepository;
import com.tromza.pokertds.service.EmailService;
import com.tromza.pokertds.service.GameService;
import com.tromza.pokertds.service.RouletteService;
import com.tromza.pokertds.service.UserService;
import com.tromza.pokertds.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final RouletteBetRepository rouletteBetRepository;
    private final GameRepository gameRepository;
    private final RouletteRepository rouletteRepository;
    private final WalletService walletService;
    private final GameService gameService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;


    public RouletteServiceImpl(RouletteBetRepository rouletteBetRepository, GameRepository gameRepository, RouletteRepository rouletteRepository, WalletService walletService, GameService gameService, UserService userService, UserRepository userRepository, EmailService emailService) {
        this.rouletteBetRepository = rouletteBetRepository;
        this.gameRepository = gameRepository;
        this.rouletteRepository = rouletteRepository;
        this.walletService = walletService;
        this.gameService = gameService;
        this.userService = userService;
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
                userService.addGameToUser(user, game);
                rouletteGame.setSpin(0);
                rouletteGame.setWins(0);
                rouletteGame.setLosses(0);
                rouletteGame.setChanged(new Timestamp(System.currentTimeMillis()));
                return rouletteRepository.save(rouletteGame);
            }
        }
    }

    public RouletteGame play(RouletteGame rouletteGame, BetRoulette bet) {
        Random generator = new Random();
        int rouletteNumber = generator.nextInt(37);
        bet.setRouletteNumber(rouletteNumber);
        rouletteGame.setSpin(rouletteGame.getSpin() + 1);
        double amount = bet.getAmount().doubleValue();
        double result = rouletteGame.getResult();
        int wins = rouletteGame.getWins();
        int losses = rouletteGame.getLosses();
        if (bet.getType() == BetType.NUMBER) {
            if (rouletteNumber == Integer.parseInt(bet.getPlayerChoice())) {
                bet.setWinAmount(36 * amount);
                rouletteGame.setWins(wins + 1);
                rouletteGame.setResult(result + 35 * amount);
            } else {
                bet.setWinAmount(0.00);
                rouletteGame.setLosses(losses + 1);
                rouletteGame.setResult(result - amount);
            }
        } else if (bet.getType() == BetType.EVEN) {
            if (rouletteNumber == 0 || rouletteNumber % 2 != 0) {
                bet.setWinAmount(0.00);
                rouletteGame.setLosses(losses + 1);
                rouletteGame.setResult(result - amount);
            } else {
                bet.setWinAmount(2 * amount);
                rouletteGame.setWins(wins + 1);
                rouletteGame.setResult(result + amount);
            }
        } else if (rouletteNumber % 2 == 0) {
            bet.setWinAmount(0.00);
            rouletteGame.setLosses(losses + 1);
            rouletteGame.setResult(result - amount);
        } else {
            bet.setWinAmount(2 * amount);
            rouletteGame.setWins(wins + 1);
            rouletteGame.setResult(result + amount);
        }
        updateBetRoulette(bet);
        rouletteGame.setChanged(new Timestamp(System.currentTimeMillis()));
        return rouletteGame;
    }

    public RouletteGame updateRouletteGame(RouletteGame rouletteGame) {
        return rouletteRepository.saveAndFlush(rouletteGame);
    }

    public BetRoulette saveBetRoulette(BetRoulette betRoulette) {
        return rouletteBetRepository.save(betRoulette);
    }

    public void updateBetRoulette(BetRoulette betRoulette) {
        rouletteBetRepository.saveAndFlush(betRoulette);
    }

    @Transactional
    public RouletteGame finishRouletteGame(RouletteGame rouletteGame, User user, Game game) {
        {
            rouletteGame.setStatus(GameStatus.COMPLETED);
            rouletteGame.setChanged(new Timestamp(System.currentTimeMillis()));
            game.setResult(rouletteGame.getResult());
            user.setScore(user.getScore() + rouletteGame.getResult());
            gameService.finishGame(game);
            userService.saveUser(user);
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
        userService.saveUser(user);
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

    public Optional<BetRoulette> findBetRouletteById(int id) {
        return rouletteBetRepository.findById(id);
    }

    ;
}
