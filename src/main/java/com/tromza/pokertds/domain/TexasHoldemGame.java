package com.tromza.pokertds.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "texasholdem_games")
public class TexasHoldemGame {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "texasholdem_games_id_seq_gen")
    @SequenceGenerator(name = "texasholdem_games_id_seq_gen", sequenceName = "texasholdem_games_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "game_id")
    private int gameId;
    @Column(name = "bank")
    private double bank;
    @Column(name = "player_preflop")
    private String playerPreflop;
    @Column(name = "casino_preflop")
    @JsonIgnore
    private String casinoPreflop;
    @Column(name = "flop")
    private String flop;
    @Column(name = "tern")
    private String tern;
    @Column(name = "river")
    private String river;
    @Column(name = "result")
    private double result;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    @Column(name = "changed")
    private Timestamp changed;

}
