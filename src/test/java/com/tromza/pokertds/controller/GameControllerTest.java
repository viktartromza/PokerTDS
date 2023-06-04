package com.tromza.pokertds.controller;

import com.tromza.pokertds.facades.impl.GameFacadeImpl;
import com.tromza.pokertds.model.response.GameInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.security.Principal;
import java.util.ArrayList;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GameControllerTest {
    @Mock
    private GameFacadeImpl gameFacade;
    @InjectMocks
    private GameController gameController;
    private MockMvc mockMvc;
    private final Integer id = 1;
    private final Principal principal = () -> "";


    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(gameController)
                .build();
    }

    @Test
    public void getGamesFromOtherUserTest() throws Exception {
        when(gameFacade.getGamesByUserId(id)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/games")
                        .param("userId", String.valueOf(id)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(gameFacade, times(1)).getGamesByUserId(id);
    }

    @Test
    public void getGamesForUserTest() throws Exception {
        when(gameFacade.getGamesForPrincipal(principal)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/games/info")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(gameFacade, times(1)).getGamesForPrincipal(principal);
    }

    @Test
    public void getGameInfoTest() throws Exception {
        when(gameFacade.getGameInfoByGameId(id)).thenReturn(new GameInfoResponse());
        mockMvc.perform(get("/games/info/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(gameFacade, times(1)).getGameInfoByGameId(id);
    }
}