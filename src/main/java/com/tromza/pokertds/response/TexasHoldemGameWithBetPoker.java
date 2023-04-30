package com.tromza.pokertds.response;

import com.tromza.pokertds.domain.BetPoker;
import com.tromza.pokertds.domain.TexasHoldemGame;
import lombok.Data;

@Data
public class TexasHoldemGameWithBetPoker {
    private TexasHoldemGame texasHoldemGame;
    private BetPoker betPoker;

    public TexasHoldemGameWithBetPoker(TexasHoldemGame texasHoldemGame, BetPoker bet) {
    }

    public TexasHoldemGame getTexasHoldemGame() {
        return texasHoldemGame;
    }

    public void setTexasHoldemGame(TexasHoldemGame texasHoldemGame) {
        this.texasHoldemGame = texasHoldemGame;
    }

    public BetPoker getBetPoker() {
        return betPoker;
    }

    public void setBetPoker(BetPoker betPoker) {
        this.betPoker = betPoker;
    }
}
