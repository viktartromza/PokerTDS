package com.tromza.pokertds.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.request.RequestUserRegistration;
import com.tromza.pokertds.request.RequestUserUpdate;
import com.tromza.pokertds.response.ResponseOtherUserInfo;
import com.tromza.pokertds.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    private UserServiceImpl userServiceImpl;
    @InjectMocks
    private UserController userController;
    private Integer id;
    private User user;
    private RequestUserUpdate requestUserUpdate;
    private List<ResponseOtherUserInfo> users;
    private ResponseOtherUserInfo responseOtherUserInfo;
    private final Principal principal = () -> "";

    @BeforeEach
    public void init() {
        id = 1;
        requestUserUpdate = new RequestUserUpdate();
        responseOtherUserInfo = new ResponseOtherUserInfo(id, null, 0);
        user = new User();
        users = List.of(new ResponseOtherUserInfo());
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    public void getAllUsersTest() throws Exception {
        when(userServiceImpl.getAllUsersForUser()).thenReturn(users);
        mockMvc.perform(get("/users/scores"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userServiceImpl, times(1)).getAllUsersForUser();
    }

    @Test
    public void anotherUserInfo() throws Exception {
        when(userServiceImpl.otherUserInfo(id)).thenReturn(Optional.of(responseOtherUserInfo));
        mockMvc.perform(get("/users/" + id.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userServiceImpl, times(1)).otherUserInfo(id);
        when(userServiceImpl.otherUserInfo(id)).thenReturn(Optional.empty());
        mockMvc.perform(get("/users/" + id.toString()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getSelfUserInfoTest() throws Exception {
        when(userServiceImpl.getUserByLogin(principal.getName())).thenReturn(Optional.of(user));
        mockMvc.perform(get("/users/info").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userServiceImpl, times(1)).getUserByLogin(principal.getName());
        when(userServiceImpl.getUserByLogin(principal.getName())).thenReturn(Optional.empty());
        mockMvc.perform(get("/users/info").principal(principal))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void createUserTest() throws Exception {
        RequestUserRegistration requestUserRegistrationValid = new RequestUserRegistration("testUser", "testpassword", "testemail@mail.ru");
        RequestUserRegistration requestUserRegistrationNotValid = new RequestUserRegistration("testUser", "abc", "testemail@mail.ru");
        when(userServiceImpl.createUser(requestUserRegistrationValid)).thenReturn(user);
        mockMvc.perform(post("/users/registration")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(requestUserRegistrationValid)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userServiceImpl, times(1)).createUser(requestUserRegistrationValid);
        mockMvc.perform(post("/users/registration")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(requestUserRegistrationNotValid)))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void updateUserTest() throws Exception {
        when(userServiceImpl.updateUser(requestUserUpdate, principal)).thenReturn(user);
        mockMvc.perform(put("/users/update").principal(principal)
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(requestUserUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userServiceImpl, times(1)).updateUser(requestUserUpdate, principal);
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/users").principal(principal))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(userServiceImpl, times(1)).deleteUser(principal);
    }
}

