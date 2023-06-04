package com.tromza.pokertds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tromza.pokertds.facades.impl.AuthFacadeImpl;
import com.tromza.pokertds.model.request.AuthRequest;
import com.tromza.pokertds.model.response.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    private MockMvc mockMvc;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthFacadeImpl authFacade;
    private final AuthRequest authRequest = new AuthRequest("testUser", "testpass");

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();
    }

    @Test
    public void authTest() throws Exception {
        when(authFacade.getTokenForUser(authRequest)).thenReturn(new AuthResponse());
        mockMvc.perform(post("/auth")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(authRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(authFacade, times(1)).getTokenForUser(authRequest);
    }


}
