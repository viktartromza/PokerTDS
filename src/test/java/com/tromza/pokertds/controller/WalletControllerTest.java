package com.tromza.pokertds.controller;

import com.tromza.pokertds.domain.Wallet;
import com.tromza.pokertds.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.security.Principal;
import java.util.Optional;

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
    private WalletService walletService;
    @InjectMocks
    private WalletController walletController;
    private final Principal principal = () -> "";
    private final Wallet wallet = new Wallet();

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(walletController)
                .build();
    }

    @Test
    public void getWalletForUserTest() throws Exception {
        when(walletService.getWalletForUser(principal)).thenReturn(Optional.of(wallet));
        mockMvc.perform(get("/wallets/info")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(walletService, times(1)).getWalletForUser(principal);
    }

    @Test
    public void createWalletForUserTest() throws Exception {
        when(walletService.createWalletForPrincipal(principal)).thenReturn(wallet);
        mockMvc.perform(post("/wallets")
                        .principal(principal))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(walletService, times(1)).createWalletForPrincipal(principal);
    }
}
