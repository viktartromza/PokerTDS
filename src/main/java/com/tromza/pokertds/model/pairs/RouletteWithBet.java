package com.tromza.pokertds.model.pairs;

import com.tromza.pokertds.model.domain.BetRoulette;
import com.tromza.pokertds.model.domain.RouletteGame;


public class RouletteWithBet {

    private RouletteGame rouletteGame;
    private BetRoulette betRoulette;

    public RouletteWithBet(RouletteGame rouletteGame, BetRoulette betRoulette) {
        this.rouletteGame = rouletteGame;
        this.betRoulette = betRoulette;
    }

    public RouletteGame getRouletteGame() {
        return rouletteGame;
    }

    public void setRouletteGame(RouletteGame rouletteGame) {
        this.rouletteGame = rouletteGame;
    }

    public BetRoulette getBetRoulette() {
        return betRoulette;
    }

    public void setBetRoulette(BetRoulette betRoulette) {
        this.betRoulette = betRoulette;
    }
}