package com.tromza.pokertds.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class UserMoneyAmount {
    private int userId;
    private BigDecimal amount;
}
