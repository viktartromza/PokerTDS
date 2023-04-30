package com.tromza.pokertds.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "bets_poker")
public class BetPoker {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bets_poker_id_seq_gen")
    @SequenceGenerator(name = "bets_poker_id_seq_gen", sequenceName = "bets_poker_id_seq", allocationSize = 1)
    private Integer id;
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
    @DecimalMax(message = "Bet can not exceed 1000 $",
            value = "1000.00")
    @DecimalMin(message = "Minimal bet is 1 $",
            value = "1.00", inclusive = false)
    private BigDecimal playerAmount;
    @Column(name = "casino_amount")
    private BigDecimal casinoAmount;
}
