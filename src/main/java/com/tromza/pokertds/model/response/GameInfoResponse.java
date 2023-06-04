package com.tromza.pokertds.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameInfoResponse {
    private RouletteResponse rouletteGameResponse;
    private TexasHoldemResponse texasHoldemResponse;

    public GameInfoResponse(RouletteResponse rouletteGameResponse) {
        this.rouletteGameResponse = rouletteGameResponse;
    }

    public GameInfoResponse(TexasHoldemResponse texasHoldemResponse) {
        this.texasHoldemResponse = texasHoldemResponse;
    }
}
