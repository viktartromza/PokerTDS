package com.tromza.pokertds.response;

import com.tromza.pokertds.domain.BetPoker;
import com.tromza.pokertds.domain.TexasHoldemGame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TexasHoldemGameWithBetPoker {
    private TexasHoldemGame texasHoldemGame;
    private BetPoker betPoker;
    private String casinoCards;
    private String winCombination;

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