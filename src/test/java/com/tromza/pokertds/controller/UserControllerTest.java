package com.tromza.pokertds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tromza.pokertds.facades.UserFacade;
import com.tromza.pokertds.model.request.UserRegistrationRequest;
import com.tromza.pokertds.model.request.UserUpdateRequest;
import com.tromza.pokertds.model.response.UserResponse;
import com.tromza.pokertds.model.response.UserToOtherUserInfoResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Mock
    private UserFacade userFacade;
    @InjectMocks
    private UserController userController;
    private final UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
    private final Principal principal = () -> "";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    public void getAllPresentUsersTest() throws Exception {
        when(userFacade.getAllUsers()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/users/scores"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userFacade, times(1)).getAllUsers();
    }

    @Test
    public void anotherUserInfo() throws Exception {
        int id = 1;
        when(userFacade.anotherUserInfoById(id)).thenReturn(new UserToOtherUserInfoResponse());
        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userFacade, times(1)).anotherUserInfoById(id);
    }

    @Test
    public void getSelfUserInfoTest() throws Exception {
        when(userFacade.selfUserInfo(principal)).thenReturn(new UserResponse());
        mockMvc.perform(get("/users/info").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userFacade, times(1)).selfUserInfo(principal);
    }

    @Test
    public void createUserTest() throws Exception {
        UserRegistrationRequest userRegistrationRequestValid = new UserRegistrationRequest("testUser", "testpassword", "testemail@mail.ru");
        UserRegistrationRequest userRegistrationRequestNotValid = new UserRegistrationRequest("testUser", "abc", "testemail@mail.ru");
        when(userFacade.createUser(userRegistrationRequestValid)).thenReturn(new UserResponse());
        mockMvc.perform(post("/users/registration")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(userRegistrationRequestValid)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userFacade, times(1)).createUser(userRegistrationRequestValid);
        mockMvc.perform(post("/users/registration")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(userRegistrationRequestNotValid)))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void updateUserTest() throws Exception {
        when(userFacade.updateUser(userUpdateRequest, principal)).thenReturn(new UserResponse());
        mockMvc.perform(put("/users/update").principal(principal)
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(userUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userFacade, times(1)).updateUser(userUpdateRequest, principal);
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/users").principal(principal))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(userFacade, times(1)).deleteUser(principal);
    }
}

