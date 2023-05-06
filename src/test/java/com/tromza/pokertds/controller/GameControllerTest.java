package com.tromza.pokertds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.request.RequestUserUpdate;
import com.tromza.pokertds.response.ResponseOtherUserInfo;
import com.tromza.pokertds.service.GameService;
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
import java.util.List;

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
    private GameService gameService;
    @InjectMocks
    private GameController gameController;
    private MockMvc mockMvc;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
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
        when(gameService.getGamesForSingleUserById(id)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/games/")
                .param("userId", String.valueOf(id)))
                .andExpect(status().isAlreadyReported())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(gameService, times(1)).getGamesForSingleUserById(id);
    }

    @Test
    public void getGamesForUserTest() throws Exception {
        when(gameService.getGamesForSingleUser(principal)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/games/info")
                        .principal(principal))
                .andExpect(status().isAlreadyReported())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(gameService, times(1)).getGamesForSingleUser(principal);
    }
}