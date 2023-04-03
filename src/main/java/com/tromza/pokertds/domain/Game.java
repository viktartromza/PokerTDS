package com.tromza.pokertds.domain;

import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "games_id_seq_gen")
    @SequenceGenerator(name = "games_id_seq_gen", sequenceName = "games_id_seq", allocationSize = 1)
    private int id;
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
   }
