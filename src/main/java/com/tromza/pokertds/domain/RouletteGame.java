package com.tromza.pokertds.domain;

import lombok.Data;

import javax.persistence.*;

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

}
