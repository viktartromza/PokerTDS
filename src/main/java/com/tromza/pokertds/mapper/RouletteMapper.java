package com.tromza.pokertds.mapper;

import com.tromza.pokertds.domain.RouletteGame;
import com.tromza.pokertds.response.RouletteResponse;
import org.springframework.stereotype.Component;

@Component
public class RouletteMapper {
    public RouletteResponse fromRouletteGameToRouletteResponse(RouletteGame rouletteGame){
        RouletteResponse response = new RouletteResponse();
        response.setId(rouletteGame.getId());
        response.setGameId(rouletteGame.getGameId());
        response.setSpin(rouletteGame.getSpin());
        response.setWins(rouletteGame.getWins());
        response.setLosses(rouletteGame.getLosses());
        response.setStatus(rouletteGame.getStatus());
        response.setResult(rouletteGame.getResult());
        response.setChanged(rouletteGame.getChanged());
        return response;
    }
}
