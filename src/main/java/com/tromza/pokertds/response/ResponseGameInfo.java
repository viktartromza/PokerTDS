package com.tromza.pokertds.response;

import com.tromza.pokertds.domain.RouletteGame;
import com.tromza.pokertds.domain.TexasHoldemGame;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseGameInfo {
    private RouletteGame rouletteGame;
    private TexasHoldemGame texasHoldemGame;

    public ResponseGameInfo(RouletteGame rouletteGame) {
        this.rouletteGame = rouletteGame;
    }

    public ResponseGameInfo(TexasHoldemGame texasHoldemGame) {
        this.texasHoldemGame = texasHoldemGame;
    }
}
