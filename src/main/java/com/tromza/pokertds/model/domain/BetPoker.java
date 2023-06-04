package com.tromza.pokertds.model.domain;

import com.tromza.pokertds.model.enums.BetPokerType;
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
@Table(name = "bets_poker")
public class BetPoker {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bets_poker_id_seq_gen")
    @SequenceGenerator(name = "bets_poker_id_seq_gen", sequenceName = "bets_poker_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "game_id")
    private int gameId;

    @Column(name = "round")
    private int round;

    @Column(name = "type_player")
    @Enumerated(EnumType.STRING)
    private BetPokerType typePlayer;

    @Column(name = "type_casino")
    @Enumerated(EnumType.STRING)
    private BetPokerType typeCasino;

    @Column(name = "player_amount")
    private BigDecimal playerAmount;

    @Column(name = "casino_amount")
    private BigDecimal casinoAmount;
}
