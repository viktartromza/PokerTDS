package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.repository.UserRepository;
import com.tromza.pokertds.response.UserResponseOtherUserInfo;
import com.tromza.pokertds.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userServiceImpl;
    private final User user = new User();
    private final UserResponseOtherUserInfo userResponseOtherUserInfo = new UserResponseOtherUserInfo(1, "testUser", 100.00);

    @Test
    public void otherUserInfoTest() {
        user.setLogin("testUser");
        Integer id = 1;
        user.setId(id);
        user.setScore(100.00);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        Optional<UserResponseOtherUserInfo> returned = userServiceImpl.otherUserInfo(id);
        verify(userRepository, times(1)).findById(id);
        assertEquals(Optional.of(userResponseOtherUserInfo), returned);
    }
}
