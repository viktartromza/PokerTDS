package com.tromza.pokertds.facades.impl;

import com.tromza.pokertds.mapper.RouletteWithBetMapper;
import com.tromza.pokertds.model.domain.BetRoulette;
import com.tromza.pokertds.model.domain.Game;
import com.tromza.pokertds.model.domain.RouletteGame;
import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.model.domain.Wallet;
import com.tromza.pokertds.facades.RouletteFacade;
import com.tromza.pokertds.mapper.BetMapper;
import com.tromza.pokertds.mapper.RouletteMapper;
import com.tromza.pokertds.model.pairs.RouletteWithBet;
import com.tromza.pokertds.model.request.BetRouletteRequest;
import com.tromza.pokertds.model.response.RouletteResponse;
import com.tromza.pokertds.model.response.RouletteWithBetResponse;
import com.tromza.pokertds.service.GameService;
import com.tromza.pokertds.service.RouletteService;
import com.tromza.pokertds.service.UserService;
import com.tromza.pokertds.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RouletteFacadeImpl implements RouletteFacade {
    private final UserService userService;
    private final RouletteService rouletteService;
    private final RouletteMapper rouletteMapper;
    private final RouletteWithBetMapper rouletteWithBetMapper;
    private final BetMapper betMapper;
    private final WalletService walletService;
    private final GameService gameService;

    public RouletteFacadeImpl(UserService userService, RouletteService rouletteService, RouletteMapper rouletteMapper, RouletteWithBetMapper rouletteWithBetMapper, BetMapper betMapper, WalletService walletService, GameService gameService) {
        this.userService = userService;
        this.rouletteService = rouletteService;
        this.rouletteMapper = rouletteMapper;
        this.rouletteWithBetMapper = rouletteWithBetMapper;
        this.betMapper = betMapper;
        this.walletService = walletService;
        this.gameService = gameService;
    }

    public RouletteResponse createRoulette(Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login " + principal.getName() + " not found!"));
        Optional<Wallet> optionalWallet = walletService.getWalletByUserId(user.getId());
        if (optionalWallet.isEmpty() || optionalWallet.get().getBalance().doubleValue() == 0) {
            throw new UnsupportedOperationException("User with id: " + user.getId() + " has zero-balance");
        } else {
            return rouletteMapper.fromRouletteGameToRouletteResponse(rouletteService.createRouletteGameForUser(user));
        }
    }

    @Transactional
    public RouletteWithBetResponse playingGame(Principal principal, BetRouletteRequest betRouletteRequest) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login " + principal.getName() + " not found!"));
        BetRoulette bet = betMapper.fromBetRouletteRequestToBet(betRouletteRequest);
        Game game = gameService.findRouletteGameInProcess(user.getId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        if (game.getId() != bet.getGameId()) {
            throw new UnsupportedOperationException("User with login " + principal.getName() + " hasn't game with id " + bet.getGameId() + " in process");
        } else{
            RouletteGame rouletteGame = rouletteService.getRouletteGameByGameId(bet.getGameId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
            Wallet wallet = walletService.getWalletForUser(user).get();
            if (wallet.getBalance().doubleValue() + rouletteGame.getResult() - bet.getAmount().doubleValue() < 0) {
                throw new UnsupportedOperationException("Bet amount can't be more than " + (wallet.getBalance().doubleValue() + rouletteGame.getResult()) + " $.");
            } else {
                BetRoulette updBetRoulette = rouletteService.saveBetRoulette(bet);

                RouletteGame updRouletteGame = rouletteService.play(rouletteGame, updBetRoulette);
                if ((wallet.getBalance().doubleValue() + updRouletteGame.getResult()) <= 0) {
                    return rouletteWithBetMapper.fromRouletteWithBetToResponse(new RouletteWithBet(rouletteService.finishRouletteGame(updRouletteGame, user, game),rouletteService.findBetRouletteById(updBetRoulette.getId()).orElseThrow(() -> new NoSuchElementException("Bet not found!"))));
                } else {
                    return rouletteWithBetMapper.fromRouletteWithBetToResponse(new RouletteWithBet(rouletteService.updateRouletteGame(updRouletteGame),rouletteService.findBetRouletteById(updBetRoulette.getId()).orElseThrow(() -> new NoSuchElementException("Bet not found!"))));
                }
            }
        }
    }

    @Transactional
    public RouletteResponse finishRouletteGameById(int id, Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login " + principal.getName() + " not found!"));
        RouletteGame rouletteGame = rouletteService.getRouletteGameById(id).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        Game game = gameService.findRouletteGameInProcess(user.getId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        if (game.getId() != rouletteGame.getGameId()) {
            throw new UnsupportedOperationException("User with login " + user.getLogin() + " hasn't game with id " + rouletteGame.getGameId() + " in process");
        } else {
            return rouletteMapper.fromRouletteGameToRouletteResponse(rouletteService.finishRouletteGame(rouletteGame,user,game));
        }
    }
}
