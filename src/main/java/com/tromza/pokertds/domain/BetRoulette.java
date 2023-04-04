package com.tromza.pokertds.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bets_roulette")
public class BetRoulette {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bets_roulette_id_seq_gen")
    @SequenceGenerator(name = "bets_roulette_id_seq_gen", sequenceName = "bets_roulette_id_seq", allocationSize = 1)
    private Integer id;
    @Column(name = "game_id")
    private int gameId;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private BetType type;

    @Column(name = "amount")
    private double amount;
    @Column (name = "player_choise")
    private String playerChoice;
    @Column(name = "roulette_number")
    private int rouletteNumber;


}
