package com.tromza.pokertds.facades.impl;

import com.tromza.pokertds.domain.BetRoulette;
import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.RouletteGame;
import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.domain.Wallet;
import com.tromza.pokertds.facades.RouletteFacade;
import com.tromza.pokertds.gamesLogic.rouletteLogic.RoulettePlay;
import com.tromza.pokertds.mapper.BetMapper;
import com.tromza.pokertds.mapper.RouletteMapper;
import com.tromza.pokertds.request.BetRouletteRequest;
import com.tromza.pokertds.response.RouletteResponse;
import com.tromza.pokertds.response.RouletteWithBet;
import com.tromza.pokertds.service.GameService;
import com.tromza.pokertds.service.RouletteService;
import com.tromza.pokertds.service.UserService;
import com.tromza.pokertds.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final BetMapper betMapper;
    private final WalletService walletService;
    private final GameService gameService;

@Autowired
    public RouletteFacadeImpl(UserService userService, RouletteService rouletteService, RouletteMapper rouletteMapper, BetMapper betMapper, WalletService walletService, GameService gameService) {
        this.userService = userService;
        this.rouletteService = rouletteService;
        this.rouletteMapper = rouletteMapper;
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
    public RouletteWithBet playingGame(Principal principal, BetRouletteRequest betRouletteRequest) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login " + principal.getName() + " not found!"));
        BetRoulette bet = betMapper.fromBetRequestToBet(betRouletteRequest);
        Game game = gameService.findRouletteGameInProcess(user.getId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        if (game.getId() != bet.getGameId()) {
            throw new UnsupportedOperationException("User with login " + principal.getName() + " hasn't game with id " + bet.getGameId() + " in process");
        } else{
            RouletteGame rouletteGame = rouletteService.getRouletteGameByGameId(bet.getGameId()).orElseThrow(() -> new NoSuchElementException("Game not found!"));
            Wallet wallet = walletService.getWalletForUser(user).get();
            if (wallet.getBalance().doubleValue() + rouletteGame.getResult() - bet.getAmount().doubleValue() < 0) {
                throw new UnsupportedOperationException("Bet amount can't be more than " + (wallet.getBalance().doubleValue() + rouletteGame.getResult()) + " $.");
            } else {
                rouletteService.saveBetRoulette(bet);
                RouletteWithBet rouletteWithBet = new RouletteWithBet(rouletteGame, bet);
                RouletteWithBet updRouletteWithBet = RoulettePlay.play(rouletteWithBet);
                if ((wallet.getBalance().doubleValue() + updRouletteWithBet.getRouletteGame().getResult()) <= 0) {
                    updRouletteWithBet.setRouletteGame(rouletteService.finishRouletteGame(updRouletteWithBet.getRouletteGame(), user, game));
                } else {
                    rouletteService.updateRouletteGame(updRouletteWithBet.getRouletteGame());
                }
                rouletteService.updateBetRoulette(updRouletteWithBet.getBetRoulette());
                return updRouletteWithBet;
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
