package com.tromza.pokertds.response;

import com.tromza.pokertds.domain.BetRoulette;
import com.tromza.pokertds.domain.RouletteGame;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
}

