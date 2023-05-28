package com.tromza.pokertds.response;

import com.tromza.pokertds.domain.RouletteGame;
import com.tromza.pokertds.domain.TexasHoldemGame;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameInfoResponse {
    private RouletteGame rouletteGame;
    private TexasHoldemGame texasHoldemGame;

    public GameInfoResponse(RouletteGame rouletteGame) {
        this.rouletteGame = rouletteGame;
    }

    public GameInfoResponse(TexasHoldemGame texasHoldemGame) {
        this.texasHoldemGame = texasHoldemGame;
    }
}
