package com.tromza.pokertds.domain;

import com.tromza.pokertds.domain.enums.BetType;
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

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "bets_roulette")
public class BetRoulette {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bets_roulette_id_seq_gen")
    @SequenceGenerator(name = "bets_roulette_id_seq_gen", sequenceName = "bets_roulette_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "game_id")
    private int gameId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private BetType type;

    @Column(name = "amount")

    private BigDecimal amount;

    @Column(name = "roulette_number")
    private int rouletteNumber;

    @Column(name = "player_choise")
    private String playerChoice;

    @Column(name = "winning_amount")
    private double winAmount;
}
