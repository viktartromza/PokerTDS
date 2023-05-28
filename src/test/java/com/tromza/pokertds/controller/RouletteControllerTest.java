package com.tromza.pokertds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tromza.pokertds.domain.BetRoulette;
import com.tromza.pokertds.domain.RouletteGame;
import com.tromza.pokertds.response.RouletteWithBet;
import com.tromza.pokertds.service.impl.RouletteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class RouletteControllerTest {
    private MockMvc mockMvc;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Mock
    private RouletteServiceImpl rouletteService;
    @InjectMocks
    private RouletteController rouletteController;
    private final Principal principal = () -> "";
    private final RouletteGame rouletteGame = new RouletteGame();
    private final BetRoulette bet = new BetRoulette();
    private final RouletteWithBet rouletteWithBet = new RouletteWithBet();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(rouletteController)
                .build();
    }

    @Test
    public void createRouletteTest() throws Exception {
        when(rouletteService.createRouletteGameForUser(principal)).thenReturn(Optional.of(rouletteGame));
        mockMvc.perform(post("/games/roulette").principal(principal))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(rouletteService, times(1)).createRouletteGameForUser(principal);
    }

    @Test
    public void playingGameTest() throws Exception {
        bet.setAmount(BigDecimal.valueOf(1100.00));
        when(rouletteService.playingRoulette(bet, principal)).thenReturn(rouletteWithBet);
        mockMvc.perform(put("/games/roulette")
                        .principal(principal)
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(bet)))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        bet.setAmount(BigDecimal.valueOf(100.00));
        when(rouletteService.playingRoulette(bet, principal)).thenReturn(rouletteWithBet);
        mockMvc.perform(put("/games/roulette")
                        .principal(principal)
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(bet)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(rouletteService, times(1)).playingRoulette(bet, principal);
    }

    @Test
    public void finishRouletteGameByIdTest() throws Exception {
        Integer id = 1;
        when(rouletteService.finishRouletteGameById(id, principal)).thenReturn(rouletteGame);
        mockMvc.perform(put("/games/roulette/finish/"+ id)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(rouletteService, times(1)).finishRouletteGameById(id, principal);
    }
}
