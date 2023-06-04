package com.tromza.pokertds.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tromza.pokertds.facades.impl.AdminFacadeImpl;
import com.tromza.pokertds.model.request.UserMoneyAmountRequest;
import com.tromza.pokertds.model.response.WalletResponse;
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
    private AdminFacadeImpl adminFacade;

    @InjectMocks
    private AdminController adminController;
    private Integer id;
    private final UserMoneyAmountRequest userMoney = new UserMoneyAmountRequest(1, BigDecimal.ONE);
    private final WalletResponse walletResponse = new WalletResponse();


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
        verify(adminFacade, times(1)).deleteUserById(id);
    }

    @Test
    public void transferWalletTest() throws Exception {
        when(adminFacade.transferWallet(userMoney)).thenReturn(walletResponse);
        mockMvc.perform(put("/admin/wallets")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(userMoney)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(adminFacade, times(1)).transferWallet(userMoney);
    }
}
