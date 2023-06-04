package com.tromza.pokertds.model.response;

import com.tromza.pokertds.model.enums.Winner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TexasHoldemResponse {
    private int gameId;
    private double bank;
    private String playerPreflop;
    private String flop;
    private String tern;
    private String river;
    private double result;
    private Winner winner;
}
