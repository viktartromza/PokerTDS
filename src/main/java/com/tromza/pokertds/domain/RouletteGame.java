package com.tromza.pokertds.domain;

import com.tromza.pokertds.domain.enums.GameStatus;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "roulette_games")
public class RouletteGame {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roulette_games_id_seq_gen")
    @SequenceGenerator(name = "roulette_games_id_seq_gen", sequenceName = "roulette_games_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "game_id")
    private int gameId;

    @Column(name = "spin")
    private int spin;

    @Column(name = "wins")
    private int wins;

    @Column(name = "losses")
    private int losses;

    @Column(name = "result")
    private double result;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "changed")
    private Timestamp changed;
}
