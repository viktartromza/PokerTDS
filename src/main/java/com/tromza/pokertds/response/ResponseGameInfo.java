package com.tromza.pokertds.response;

import com.tromza.pokertds.domain.RouletteGame;
import lombok.Data;

@Data
public class ResponseGameInfo {
    private RouletteGame rouletteGame;

    public ResponseGameInfo(RouletteGame rouletteGame) {
        this.rouletteGame = rouletteGame;
    }
}
