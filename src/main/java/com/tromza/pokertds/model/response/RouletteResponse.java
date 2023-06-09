package com.tromza.pokertds.model.response;

import com.tromza.pokertds.model.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouletteResponse {
    private int id;
    private int gameId;
    private int spin;
    private int wins;
    private int losses;
    private double result;
    private GameStatus status;
    private Timestamp changed;
}
