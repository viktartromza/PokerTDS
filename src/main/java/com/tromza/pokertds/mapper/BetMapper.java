package com.tromza.pokertds.mapper;

import com.tromza.pokertds.model.domain.BetPoker;
import com.tromza.pokertds.model.domain.BetRoulette;
import com.tromza.pokertds.model.request.BetPokerRequest;
import com.tromza.pokertds.model.request.BetRouletteRequest;
import org.springframework.stereotype.Component;

@Component
public class BetMapper {
    public BetRoulette fromBetRouletteRequestToBet(BetRouletteRequest request) {
        BetRoulette betRoulette = new BetRoulette();
        betRoulette.setGameId(request.getGameId());
        betRoulette.setType(request.getType());
        betRoulette.setAmount(request.getAmount());
        if (request.getPlayerChoice()!=null) {
            betRoulette.setPlayerChoice(request.getPlayerChoice());
        }
        return betRoulette;
    }

    public BetPoker fromBetPokerRequestToBet(BetPokerRequest request) {
        BetPoker betPoker = new BetPoker();
        betPoker.setGameId(request.getGameId());
        betPoker.setTypePlayer(request.getTypePlayer());
        betPoker.setPlayerAmount(request.getPlayerAmount());
        return betPoker;
    }

}
