package com.tromza.pokertds.model.response;

import com.tromza.pokertds.model.enums.BetType;
import com.tromza.pokertds.model.enums.GameStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RouletteWithBetResponse {
    private int rouletteId;
    private int gameId;
    private int spin;
    private int wins;
    private int losses;
    private double result;
    private GameStatus status;
    private int betRouletteId;
    private BetType type;
    private BigDecimal amount;
    private int rouletteNumber;
    private String playerChoice;
    private double winAmount;
    }

