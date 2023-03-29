package com.tromza.pokertds.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Component
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "wallets_id_seq_gen")
    private int id;
    private int userId;
    private BigDecimal balance;
}
