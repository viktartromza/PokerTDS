package com.tromza.pokertds.mapper;

import com.tromza.pokertds.domain.BetRoulette;
import com.tromza.pokertds.request.BetRouletteRequest;
import org.springframework.stereotype.Component;

@Component
public class BetMapper {
    public BetRoulette fromBetRequestToBet(BetRouletteRequest request) {
        BetRoulette betRoulette = new BetRoulette();
        betRoulette.setGameId(request.getGameId());
        betRoulette.setType(request.getType());
        betRoulette.setAmount(request.getAmount());
        if (!request.getPlayerChoice().isEmpty()) {
            betRoulette.setPlayerChoice(request.getPlayerChoice());
        }
        return betRoulette;
    }
}
