package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.*;
import com.tromza.pokertds.repository.TexasHoldemRepository;
import com.tromza.pokertds.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public TexasHoldemService(WalletService walletService, GameService gameService, UserService userService, TexasHoldemRepository texasHoldemRepository, UserRepository userRepository, EmailService emailService) {
        this.walletService = walletService;
        this.gameService = gameService;
        this.userService = userService;
        this.texasHoldemRepository = texasHoldemRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public Optional<TexasHoldemGame> createTexasHoldemGameForUser(Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login not found!"));
        if (gameService.findTexasHoldemGameInProcess(user.getId()).isPresent()) {
            log.info("User with id: " + user.getId() + " has texas holdem in process");
            return texasHoldemRepository.findTexasHoldemGameByGameId(gameService.findRouletteGameInProcess(user.getId()).get().getId());
        } else {
            Optional<Wallet> optionalWallet = walletService.getWalletByUserId(user.getId());
            if (optionalWallet.isEmpty() || optionalWallet.get().getBalance().doubleValue() == 0) {
                throw new UnsupportedOperationException("User with id: " + user.getId() + " has zero-balance");
            } else {
                Game game = new Game();
                game.setType(GameType.TEXAS_HOLDEM);
                game = gameService.createGame(game);
                TexasHoldemGame texasHoldemGame = new TexasHoldemGame();
                texasHoldemGame.setGameId(game.getId());
                texasHoldemGame.setStatus(GameStatus.IN_PROCESS);
                userService.addGameToUser(user, game);
                texasHoldemGame.setBank(2 * BLIND);//TODO wallet balance etc

                return Optional.of(texasHoldemRepository.save(texasHoldemGame));
            }
        }
    }
}
