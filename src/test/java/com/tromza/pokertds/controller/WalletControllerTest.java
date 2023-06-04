package com.tromza.pokertds.controller;

import com.tromza.pokertds.facades.WalletFacade;
import com.tromza.pokertds.model.response.WalletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.security.Principal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {
    private MockMvc mockMvc;
    @Mock
    private WalletFacade walletFacade;
    @InjectMocks
    private WalletController walletController;
    private final Principal principal = () -> "";


    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(walletController)
                .build();
    }

    @Test
    public void getWalletForUserTest() throws Exception {
        when(walletFacade.getWalletForUser(principal)).thenReturn(new WalletResponse());
        mockMvc.perform(get("/wallets/info")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(walletFacade, times(1)).getWalletForUser(principal);
    }

    @Test
    public void createWalletForUserTest() throws Exception {
        when(walletFacade.createWalletForUser(principal)).thenReturn(new WalletResponse());
        mockMvc.perform(post("/wallets")
                        .principal(principal))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(walletFacade, times(1)).createWalletForUser(principal);
    }
}
