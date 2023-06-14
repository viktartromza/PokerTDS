package com.tromza.pokertds.service;

import com.tromza.pokertds.model.domain.BetPoker;
import com.tromza.pokertds.model.domain.TexasHoldemGame;
import com.tromza.pokertds.model.domain.User;


import java.util.Optional;

public interface TexasHoldemService {
    Optional<TexasHoldemGame> getTexasHoldemGameByGameId (int id);
    TexasHoldemGame createTexasHoldemGameForUser(User user);

    TexasHoldemGame playingTexasHoldem(BetPoker bet, User user) throws InterruptedException;

    TexasHoldemGame finishTexasHoldemGame(TexasHoldemGame texasHoldemGame, User user);

    TexasHoldemGame updateTexasHoldemGame(TexasHoldemGame texasHoldemGame);

    TexasHoldemGame playTexasHoldem(TexasHoldemGame texasHoldemGame, BetPoker bet, User user) throws InterruptedException;

    void saveBetPoker(BetPoker bet);

    Optional<BetPoker> findLastBetPokerByGameId(int id);

    void findNoPlayedTexasHoldemGames();
    void finishNoPlayedTexasHoldemGames(TexasHoldemGame texasHoldemGame);
    String winCombination(String hand, String board) throws InterruptedException;
}
