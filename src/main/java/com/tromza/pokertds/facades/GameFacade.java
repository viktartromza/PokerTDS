package com.tromza.pokertds.facades;

import com.tromza.pokertds.response.GameInfoResponse;
import com.tromza.pokertds.response.GameResponse;

import java.security.Principal;
import java.util.List;

public interface GameFacade {
    List<GameResponse> getGamesByUserId (int userId);

    List<GameResponse> getGamesForPrincipal (Principal principal);

    GameInfoResponse getGameInfoByGameId (int gameId);
}
