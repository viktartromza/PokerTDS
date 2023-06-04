package com.tromza.pokertds.mapper;

import com.tromza.pokertds.model.domain.TexasHoldemGame;
import com.tromza.pokertds.model.response.TexasHoldemResponse;
import org.springframework.stereotype.Component;

@Component
public class TexasHoldemMapper {
    public TexasHoldemResponse fromTexasHoldemToTexasHoldemResponse(TexasHoldemGame game) {
        TexasHoldemResponse response = new TexasHoldemResponse();
        response.setGameId(game.getGameId());
        response.setBank(game.getBank());
        response.setPlayerPreflop(game.getPlayerPreflop());
        if (game.getFlop()!=null) {
            response.setFlop(game.getFlop());
        }
        if (game.getTern()!=null) {
            response.setTern(game.getTern());
        }
        if (game.getRiver()!=null) {
            response.setRiver(game.getRiver());
        }
        response.setResult(game.getResult());
        if (game.getWinner()!=null) {
            response.setWinner(game.getWinner());
        }
        return response;
    }
}
