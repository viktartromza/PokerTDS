package com.tromza.pokertds.domain;

import lombok.Data;
import org.springframework.stereotype.Component;
@Data
@Component
public class Wallet {
    private long id;
    private double balance;
}
