package com.tromza.pokertds.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tromza.pokertds.domain.Wallet;
import com.tromza.pokertds.request.UserMoneyAmount;
import com.tromza.pokertds.service.UserService;
import com.tromza.pokertds.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    private MockMvc mockMvc;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Mock
    private UserService userService;
    @Mock
    private WalletService walletService;
    @InjectMocks
    private AdminController adminController;
    private Integer id;
    private final UserMoneyAmount userMoney = new UserMoneyAmount(1, BigDecimal.ONE);
    private final Wallet wallet = new Wallet();

    @BeforeEach
    public void init() {
        id = 1;
        mockMvc = MockMvcBuilders
                .standaloneSetup(adminController)
                .build();
    }

    @Test
    public void deleteUserByIdForAdminTest() throws Exception {
        mockMvc.perform(delete("/admin/users/" + id))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(userService, times(1)).deleteUserByIdForAdmin(id);
    }
    @Test
    public void transferWalletTest() throws Exception {
        when(walletService.updateWallet(userMoney)).thenReturn(wallet);
        mockMvc.perform(put("/admin/wallets")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(userMoney)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(walletService, times(1)).updateWallet(userMoney);
    }
}
