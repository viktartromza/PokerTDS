package com.tromza.pokertds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tromza.pokertds.model.enums.BetType;
import com.tromza.pokertds.facades.RouletteFacade;
import com.tromza.pokertds.model.request.BetRouletteRequest;
import com.tromza.pokertds.model.response.RouletteResponse;
import com.tromza.pokertds.model.response.RouletteWithBetResponse;
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
    private RouletteFacade rouletteFacade;
    @InjectMocks
    private RouletteController rouletteController;
    private final Principal principal = () -> "";
    private final BetRouletteRequest betRouletteRequest = new BetRouletteRequest(1, BetType.NUMBER, BigDecimal.valueOf(50.0), "0");
    private final RouletteResponse rouletteResponse = new RouletteResponse();
    private final RouletteWithBetResponse rouletteWithBetResponse = new RouletteWithBetResponse();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(rouletteController)
                .build();
    }

    @Test
    public void createRouletteTest() throws Exception {
        when(rouletteFacade.createRoulette(principal)).thenReturn(rouletteResponse);
        mockMvc.perform(post("/games/roulette").principal(principal))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(rouletteFacade, times(1)).createRoulette(principal);
    }

    @Test
    public void playingGameTest() throws Exception {
        betRouletteRequest.setAmount(BigDecimal.valueOf(1100.00));
        when(rouletteFacade.playingGame(principal, betRouletteRequest)).thenReturn(rouletteWithBetResponse);
        mockMvc.perform(put("/games/roulette")
                        .principal(principal)
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(betRouletteRequest)))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        betRouletteRequest.setAmount(BigDecimal.valueOf(100.00));
        when(rouletteFacade.playingGame(principal, betRouletteRequest)).thenReturn(rouletteWithBetResponse);
        mockMvc.perform(put("/games/roulette")
                        .principal(principal)
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(betRouletteRequest)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(rouletteFacade, times(1)).playingGame(principal, betRouletteRequest);
    }

    @Test
    public void finishRouletteGameByIdTest() throws Exception {
        int id = 1;
        when(rouletteFacade.finishRouletteGameById(id, principal)).thenReturn(rouletteResponse);
        mockMvc.perform(put("/games/roulette/finish/" + id)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(rouletteFacade, times(1)).finishRouletteGameById(id, principal);
    }
}
