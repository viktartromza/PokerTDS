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
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

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
    @DecimalMax(message = "Bet can not exceed 1000 $",
            value = "1000.00")
    @DecimalMin(message = "Minimal bet is 1 $",
            value = "1.00")
    private BigDecimal amount;

    @Column(name = "roulette_number")
    private int rouletteNumber;

    @Column(name = "player_choise")
    private String playerChoice;

    @Column(name = "winning_amount")
    private double winAmount;
}
