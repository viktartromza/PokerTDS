package com.tromza.pokertds.model.request;

import com.tromza.pokertds.model.enums.BetPokerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetPokerRequest {
    private int gameId;
    private BetPokerType typePlayer;

    @DecimalMax(message = "Bet can not exceed 1000 $",
            value = "1000.00")
    @DecimalMin(message = "Minimal bet is 1 $",
            value = "1.00")
    private BigDecimal playerAmount;
}
