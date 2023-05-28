package com.tromza.pokertds.gamesLogic.rouletteLogic;

import com.tromza.pokertds.domain.enums.BetType;
import com.tromza.pokertds.response.RouletteWithBet;

import java.sql.Timestamp;
import java.util.Random;

public class RoulettePlay {
    public static RouletteWithBet play (RouletteWithBet rouletteWithBet) {
        Random generator = new Random();
        int rouletteNumber = generator.nextInt(37);
        rouletteWithBet.getBetRoulette().setRouletteNumber(rouletteNumber);
        rouletteWithBet.getRouletteGame().setSpin(rouletteWithBet.getRouletteGame().getSpin() + 1);
        double amount = rouletteWithBet.getBetRoulette().getAmount().doubleValue();
        double result = rouletteWithBet.getRouletteGame().getResult();
        int wins = rouletteWithBet.getRouletteGame().getWins();
        int losses = rouletteWithBet.getRouletteGame().getLosses();
        if (rouletteWithBet.getBetRoulette().getType() == BetType.NUMBER) {
            if (rouletteNumber == Integer.parseInt(rouletteWithBet.getBetRoulette().getPlayerChoice())) {
                rouletteWithBet.getBetRoulette().setWinAmount(36 * amount);
                rouletteWithBet.getRouletteGame().setWins(wins + 1);
                rouletteWithBet.getRouletteGame().setResult(result + 35 * amount);
            } else {
                rouletteWithBet.getBetRoulette().setWinAmount(0.00);
                rouletteWithBet.getRouletteGame().setLosses(losses + 1);
                rouletteWithBet.getRouletteGame().setResult(result - amount);
            }
        } else if (rouletteWithBet.getBetRoulette().getType() == BetType.EVEN) {
            if (rouletteNumber == 0 || rouletteNumber % 2 != 0) {
                rouletteWithBet.getBetRoulette().setWinAmount(0.00);
                rouletteWithBet.getRouletteGame().setLosses(losses + 1);
                rouletteWithBet.getRouletteGame().setResult(result - amount);
            } else {
                rouletteWithBet.getBetRoulette().setWinAmount(2 * amount);
                rouletteWithBet.getRouletteGame().setWins(wins + 1);
                rouletteWithBet.getRouletteGame().setResult(result + amount);
            }
        } else if (rouletteNumber % 2 == 0) {
            rouletteWithBet.getBetRoulette().setWinAmount(0.00);
            rouletteWithBet.getRouletteGame().setLosses(losses + 1);
            rouletteWithBet.getRouletteGame().setResult(result - amount);
        } else {
            rouletteWithBet.getBetRoulette().setWinAmount(2 * amount);
            rouletteWithBet.getRouletteGame().setWins(wins + 1);
            rouletteWithBet.getRouletteGame().setResult(result + amount);
        }
        rouletteWithBet.getRouletteGame().setChanged(new Timestamp(System.currentTimeMillis()));
        return rouletteWithBet;
    }
}
