package com.tromza.pokertds.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallets_id_seq_gen")
    @SequenceGenerator(name = "wallets_id_seq_gen", sequenceName = "wallets_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "user_id")
    private int userId;
}
