package com.tromza.pokertds.domain;

import com.tromza.pokertds.domain.enums.GameStatus;
import com.tromza.pokertds.domain.enums.GameType;
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
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "games_id_seq_gen")
    @SequenceGenerator(name = "games_id_seq_gen", sequenceName = "games_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private GameType type;

    @Column(name = "time_create")
    private Timestamp create;

    @Column(name = "finish")
    private Timestamp finish;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "result")
    private Double result;
   }
