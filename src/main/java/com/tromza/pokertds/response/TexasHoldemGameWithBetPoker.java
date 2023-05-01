package com.tromza.pokertds.response;

import com.tromza.pokertds.domain.BetPoker;
import com.tromza.pokertds.domain.TexasHoldemGame;
import lombok.Data;

@Data
public class TexasHoldemGameWithBetPoker {
    private TexasHoldemGame texasHoldemGame;
    private BetPoker betPoker;
    private String casinoCards;

    public TexasHoldemGameWithBetPoker(TexasHoldemGame texasHoldemGame, BetPoker betPoker) {
        this.texasHoldemGame = texasHoldemGame;
        this.betPoker = betPoker;
    }

    public TexasHoldemGameWithBetPoker(TexasHoldemGame texasHoldemGame, BetPoker betPoker, String casinoCards) {
        this.texasHoldemGame = texasHoldemGame;
        this.betPoker = betPoker;
        this.casinoCards = casinoCards;
    }
  }