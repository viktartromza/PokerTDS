package com.tromza.pokertds.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserMoneyAmount {
    private int userId;
    private BigDecimal amount;
}
