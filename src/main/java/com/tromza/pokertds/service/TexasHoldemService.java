package com.tromza.pokertds.service;

import com.tromza.pokertds.model.domain.BetPoker;
import com.tromza.pokertds.model.domain.TexasHoldemGame;
import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.model.pairs.TexasHoldemGameWithBetPoker;

import java.util.Optional;

public interface TexasHoldemService {
    Optional<TexasHoldemGame> getTexasHoldemGameByGameId (int id);
    TexasHoldemGame createTexasHoldemGameForUser(User user);

    TexasHoldemGameWithBetPoker playingTexasHoldem(BetPoker bet, User user) throws InterruptedException;

    TexasHoldemGame finishTexasHoldemGame(TexasHoldemGame texasHoldemGame, User user);

    TexasHoldemGame updateTexasHoldemGame(TexasHoldemGame texasHoldemGame);

    TexasHoldemGameWithBetPoker playTexasHoldem(TexasHoldemGame texasHoldemGame, BetPoker bet, User user) throws InterruptedException;

    BetPoker saveBetPoker(BetPoker bet);

    void findNoPlayedTexasHoldemGames();
    void finishNoPlayedTexasHoldemGames(TexasHoldemGame texasHoldemGame);
}
