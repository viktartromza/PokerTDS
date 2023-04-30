package com.tromza.pokertds.response;

import com.tromza.pokertds.domain.BetRoulette;
import com.tromza.pokertds.domain.RouletteGame;
import lombok.Data;

@Data
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

