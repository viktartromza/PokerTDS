package com.tromza.pokertds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tromza.pokertds.domain.BetPoker;
import com.tromza.pokertds.domain.TexasHoldemGame;
import com.tromza.pokertds.response.TexasHoldemGameWithBetPoker;
import com.tromza.pokertds.service.TexasHoldemService;
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
public class TexasHoldemControllerTest {
    private MockMvc mockMvc;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Mock
    private TexasHoldemService texasHoldemService;
    @InjectMocks
    private TexasHoldemController texasHoldemController;
    private final Principal principal = () -> "";
    private final TexasHoldemGame texasHoldemGame = new TexasHoldemGame();
    private final BetPoker bet = new BetPoker();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(texasHoldemController)
                .build();
    }

    @Test
    public void createTexasHoldemTest() throws Exception {
        when(texasHoldemService.createTexasHoldemGameForUser(principal)).thenReturn(Optional.of(texasHoldemGame));
        mockMvc.perform(post("/games/poker/texas")
                        .principal(principal))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(texasHoldemService, times(1)).createTexasHoldemGameForUser(principal);
    }

    @Test
    public void playingGameTest() throws Exception {
        bet.setPlayerAmount(BigDecimal.valueOf(1100.00));
        when(texasHoldemService.playingTexasHoldem(bet, principal)).thenReturn(new TexasHoldemGameWithBetPoker());
        mockMvc.perform(put("/games/poker/texas")
                        .principal(principal)
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(bet)))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        bet.setPlayerAmount(BigDecimal.valueOf(100.00));
        when(texasHoldemService.playingTexasHoldem(bet, principal)).thenReturn(new TexasHoldemGameWithBetPoker());
        mockMvc.perform(put("/games/poker/texas")
                        .principal(principal)
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(bet)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(texasHoldemService, times(1)).playingTexasHoldem(bet,principal);
    }
}
