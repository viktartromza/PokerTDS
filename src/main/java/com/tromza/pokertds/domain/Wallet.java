package com.tromza.pokertds.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "wallets_id_seq_gen")
    private int id;

    @Column(name = "balance")
    private BigDecimal balance;
    @OneToOne(mappedBy = "wallet")
    private User user;

}
