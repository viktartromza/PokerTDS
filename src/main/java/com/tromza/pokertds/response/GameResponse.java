package com.tromza.pokertds.response;

import com.tromza.pokertds.domain.enums.GameStatus;
import com.tromza.pokertds.domain.enums.GameType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {

    private Integer id;

    private GameType type;

    private Timestamp create;

    private Timestamp finish;

    private GameStatus status;

    private Double result;
}
