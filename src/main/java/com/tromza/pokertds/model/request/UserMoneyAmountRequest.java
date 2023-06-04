package com.tromza.pokertds.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserMoneyAmountRequest {
    private int userId;
    private BigDecimal amount;
}
