package com.tromza.pokertds.service;

import com.tromza.pokertds.model.domain.Game;
import com.tromza.pokertds.model.domain.User;

import java.util.List;
import java.util.Optional;

public interface GameService {
    Optional<Game> getGameById(int id);

    void finishGame(Game game);

    Game createGame(Game game);

    List<Game> getGamesForSingleUserById(int id);

    List<Game> getGamesForSingleUser(User user);

    Optional<Game> findRouletteGameInProcess(int userId);

    Optional<Game> findTexasHoldemGameInProcess(int userId);
}
