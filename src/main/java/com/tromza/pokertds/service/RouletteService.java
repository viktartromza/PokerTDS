package com.tromza.pokertds.service;

import com.tromza.pokertds.model.domain.BetRoulette;
import com.tromza.pokertds.model.domain.Game;
import com.tromza.pokertds.model.domain.RouletteGame;
import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.model.pairs.RouletteWithBet;

import java.util.Optional;

public interface RouletteService {
    Optional<RouletteGame> getRouletteGameById(int id);

    Optional<RouletteGame> getRouletteGameByGameId(int id);

    RouletteGame createRouletteGameForUser(User user);
    RouletteWithBet play (RouletteWithBet rouletteWithBet);

    RouletteGame updateRouletteGame(RouletteGame rouletteGame);

    void saveBetRoulette(BetRoulette betRoulette);

    void updateBetRoulette(BetRoulette betRoulette);

    RouletteGame finishRouletteGame(RouletteGame rouletteGame, User user, Game game);

    void finishRouletteGameAutomatically(RouletteGame rouletteGame);

    void findNoPlayedRouletteGames();

    void finishNoPlayedRouletteGames();
}
