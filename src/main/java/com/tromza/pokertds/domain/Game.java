package com.tromza.pokertds.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class Game {
    private int id;
    private String type;
    private Date date;
}
