package com.tromza.pokertds.model.response;

import com.tromza.pokertds.model.enums.BetPokerType;
import com.tromza.pokertds.model.enums.Winner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TexasHoldemWihtBetResponse {
    private int texasHoldemId;
    private int gameId;
    private double bank;
    private double playerDeposit;
    private String playerPreflop;
    private String flop;
    private String tern;
    private String river;
    private double result;
    private Winner winner;
    private int round;
    private BetPokerType typePlayer;
    private BetPokerType typeCasino;
    private BigDecimal playerAmount;
    private BigDecimal casinoAmount;
    private String casinoCards;
    private String winCombination;
}
