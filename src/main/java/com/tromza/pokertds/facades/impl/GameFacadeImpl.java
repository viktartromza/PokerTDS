package com.tromza.pokertds.facades.impl;

import com.tromza.pokertds.facades.GameFacade;
import com.tromza.pokertds.mapper.GameMapper;
import com.tromza.pokertds.response.GameInfoResponse;
import com.tromza.pokertds.response.GameResponse;
import com.tromza.pokertds.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class GameFacadeImpl implements GameFacade {

    private final GameService gameService;
    private final GameMapper gameMapper;
@Autowired
    public GameFacadeImpl(GameService gameService, GameMapper gameMapper) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }

    public List<GameResponse> getGamesByUserId(int userId) {
        return gameService.getGamesForSingleUserById(userId).stream().map(gameMapper::gameResponse).collect(Collectors.toList());
    }

    public List<GameResponse> getGamesForPrincipal(Principal principal) {
        return gameService.getGamesForSingleUser(principal).stream().map(gameMapper::gameResponse).collect(Collectors.toList());
    }

    public GameInfoResponse getGameInfoByGameId(int gameId) {
        return gameService.getGameInfoById(gameId);
    }
}
