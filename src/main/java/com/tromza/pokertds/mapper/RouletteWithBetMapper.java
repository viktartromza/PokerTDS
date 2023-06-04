package com.tromza.pokertds.mapper;

import com.tromza.pokertds.model.pairs.RouletteWithBet;
import com.tromza.pokertds.model.response.RouletteWithBetResponse;
import org.springframework.stereotype.Component;

@Component
public class RouletteWithBetMapper {
    public RouletteWithBetResponse fromRouletteWithBetToResponse(RouletteWithBet rouletteWithBet) {
        RouletteWithBetResponse response = new RouletteWithBetResponse();
        response.setRouletteId(rouletteWithBet.getRouletteGame().getId());
        response.setGameId(rouletteWithBet.getRouletteGame().getGameId());
        response.setSpin(rouletteWithBet.getRouletteGame().getSpin());
        response.setWins(rouletteWithBet.getRouletteGame().getWins());
        response.setLosses(rouletteWithBet.getRouletteGame().getLosses());
        response.setResult(rouletteWithBet.getRouletteGame().getResult());
        response.setStatus(rouletteWithBet.getRouletteGame().getStatus());
        response.setBetRouletteId(rouletteWithBet.getBetRoulette().getId());
        response.setAmount(rouletteWithBet.getBetRoulette().getAmount());
        response.setRouletteNumber(rouletteWithBet.getBetRoulette().getRouletteNumber());
        if(!rouletteWithBet.getBetRoulette().getPlayerChoice().isEmpty()) {
            response.setPlayerChoice(rouletteWithBet.getBetRoulette().getPlayerChoice());
        }
        response.setWinAmount(rouletteWithBet.getBetRoulette().getWinAmount());
        return response;
    }
}
