package com.tromza.pokertds.facades.impl;

import com.tromza.pokertds.mapper.RouletteMapper;
import com.tromza.pokertds.mapper.TexasHoldemMapper;
import com.tromza.pokertds.model.domain.Game;
import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.facades.GameFacade;
import com.tromza.pokertds.mapper.GameMapper;
import com.tromza.pokertds.model.response.GameInfoResponse;
import com.tromza.pokertds.model.response.GameResponse;
import com.tromza.pokertds.service.RouletteService;
import com.tromza.pokertds.service.TexasHoldemService;
import com.tromza.pokertds.service.UserService;
import com.tromza.pokertds.service.impl.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class GameFacadeImpl implements GameFacade {
    private final GameServiceImpl gameService;
    private final GameMapper gameMapper;
    private final RouletteMapper rouletteMapper;
    private final TexasHoldemMapper texasHoldemMapper;
    private final UserService userService;
    private final RouletteService rouletteService;
    private final TexasHoldemService texasHoldemService;

    @Autowired
    public GameFacadeImpl(GameServiceImpl gameService, GameMapper gameMapper, RouletteMapper rouletteMapper, TexasHoldemMapper texasHoldemMapper, UserService userService, RouletteService rouletteService, TexasHoldemService texasHoldemService) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
        this.rouletteMapper = rouletteMapper;
        this.texasHoldemMapper = texasHoldemMapper;
        this.userService = userService;
        this.rouletteService = rouletteService;
        this.texasHoldemService = texasHoldemService;
    }

    public List<GameResponse> getGamesByUserId(int userId) {
        return gameService.getGamesForSingleUserById(userId).stream().map(gameMapper::gameResponse).collect(Collectors.toList());
    }

    public List<GameResponse> getGamesForPrincipal(Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login " + principal.getName() + " not found!"));
        return gameService.getGamesForSingleUser(user).stream().map(gameMapper::gameResponse).collect(Collectors.toList());
    }

    public GameInfoResponse getGameInfoByGameId(int gameId) {
        Game game = gameService.getGameById(gameId).orElseThrow(() -> new NoSuchElementException("Game not found!"));
        return switch (game.getType()) {
            case ROULETTE_EU ->
                    new GameInfoResponse(rouletteMapper.fromRouletteGameToRouletteResponse(rouletteService.getRouletteGameByGameId(gameId).orElseThrow(() -> new NoSuchElementException("Roulette-game not found!"))));
            case TEXAS_HOLDEM ->
                    new GameInfoResponse(texasHoldemMapper.fromTexasHoldemToTexasHoldemResponse(texasHoldemService.getTexasHoldemGameByGameId(gameId).orElseThrow(() -> new NoSuchElementException("TexasHoldem-game not found!"))));
            default -> throw new NoSuchElementException("Game not found!");
        };
    }
}
