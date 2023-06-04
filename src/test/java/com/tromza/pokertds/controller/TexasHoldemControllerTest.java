package com.tromza.pokertds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tromza.pokertds.facades.TexasHoldemFacade;
import com.tromza.pokertds.model.request.BetPokerRequest;
import com.tromza.pokertds.model.response.TexasHoldemResponse;
import com.tromza.pokertds.model.response.TexasHoldemWihtBetResponse;
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
public class TexasHoldemControllerTest {
    private MockMvc mockMvc;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Mock
    private TexasHoldemFacade texasHoldemFacade;
    @InjectMocks
    private TexasHoldemController texasHoldemController;
    private final Principal principal = () -> "";
    private final TexasHoldemResponse texasHoldemGameResponse = new TexasHoldemResponse();
    private final BetPokerRequest betPokerRequest = new BetPokerRequest();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(texasHoldemController)
                .build();
    }

    @Test
    public void createTexasHoldemTest() throws Exception {
        when(texasHoldemFacade.createTexasHoldem(principal)).thenReturn(texasHoldemGameResponse);
        mockMvc.perform(post("/games/poker/texas")
                        .principal(principal))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(texasHoldemFacade, times(1)).createTexasHoldem(principal);
    }

    @Test
    public void playingGameTest() throws Exception {
        betPokerRequest.setPlayerAmount(BigDecimal.valueOf(1100.00));
        when(texasHoldemFacade.playingGame(principal, betPokerRequest)).thenReturn(new TexasHoldemWihtBetResponse());
        mockMvc.perform(put("/games/poker/texas")
                        .principal(principal)
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(betPokerRequest)))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        betPokerRequest.setPlayerAmount(BigDecimal.valueOf(100.00));
        when(texasHoldemFacade.playingGame(principal, betPokerRequest)).thenReturn(new TexasHoldemWihtBetResponse());
        mockMvc.perform(put("/games/poker/texas")
                        .principal(principal)
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(betPokerRequest)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(texasHoldemFacade, times(1)).playingGame(principal, betPokerRequest);
    }
}
