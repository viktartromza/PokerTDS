package com.tromza.pokertds.mapper;

import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.response.GameResponse;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    public GameResponse gameResponse(Game game) {
        GameResponse gameResponse = new GameResponse();
        gameResponse.setId(game.getId());
        gameResponse.setType(game.getType());
        gameResponse.setCreate(game.getCreate());
        gameResponse.setFinish(game.getFinish());
        gameResponse.setStatus(game.getStatus());
        gameResponse.setResult(game.getResult());
        return gameResponse;
    }
}
