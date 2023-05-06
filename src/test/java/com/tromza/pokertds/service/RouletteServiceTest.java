package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.BetRoulette;
import com.tromza.pokertds.domain.RouletteGame;
import com.tromza.pokertds.response.RouletteWithBet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class RouletteServiceTest {
    @InjectMocks
    private RouletteService rouletteService;
    private final RouletteGame rouletteGame = new RouletteGame();
    private final BetRoulette bet = new BetRoulette();

    @Test
    public void rouletteNumberTest() {
        bet.setAmount(BigDecimal.ONE);
        int rouletteNumber;
        for (int i = 0; i <= 1000000; i++) {
            rouletteNumber = rouletteService.play(new RouletteWithBet(rouletteGame, bet)).getBetRoulette().getRouletteNumber();
            assertTrue(rouletteNumber >= 0 && rouletteNumber <= 36);
        }
    }
}
