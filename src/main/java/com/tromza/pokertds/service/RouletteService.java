package com.tromza.pokertds.service;

import com.tromza.pokertds.model.domain.BetRoulette;
import com.tromza.pokertds.model.domain.Game;
import com.tromza.pokertds.model.domain.RouletteGame;
import com.tromza.pokertds.model.domain.User;

import java.util.Optional;

public interface RouletteService {
    Optional<RouletteGame> getRouletteGameById(int id);

    Optional<RouletteGame> getRouletteGameByGameId(int id);

    RouletteGame createRouletteGameForUser(User user);
    RouletteGame play (RouletteGame rouletteGame, BetRoulette betRoulette);

    RouletteGame updateRouletteGame(RouletteGame rouletteGame);

    BetRoulette saveBetRoulette(BetRoulette betRoulette);

    void updateBetRoulette(BetRoulette betRoulette);

    Optional<BetRoulette> findBetRouletteById (int id);

    RouletteGame finishRouletteGame(RouletteGame rouletteGame, User user, Game game);

    void finishRouletteGameAutomatically(RouletteGame rouletteGame);

    void findNoPlayedRouletteGames();

    void finishNoPlayedRouletteGames();
}
