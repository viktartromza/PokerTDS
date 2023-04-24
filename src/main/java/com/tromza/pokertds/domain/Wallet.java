package com.tromza.pokertds.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
//@ToString(exclude = {"user"})
//@EqualsAndHashCode(exclude = {"user"})
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "wallets_id_seq_gen")
    private int id;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "user_id")
    private int userId;

}
