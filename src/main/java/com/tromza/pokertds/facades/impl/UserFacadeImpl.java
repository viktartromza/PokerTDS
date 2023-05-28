package com.tromza.pokertds.facades.impl;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.facades.UserFacade;
import com.tromza.pokertds.mapper.UserMapper;
import com.tromza.pokertds.request.RequestUserRegistration;
import com.tromza.pokertds.request.RequestUserUpdate;
import com.tromza.pokertds.response.UserResponse;
import com.tromza.pokertds.response.UserResponseOtherUserInfo;
import com.tromza.pokertds.service.UserService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserFacadeImpl(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public List<UserResponseOtherUserInfo> getAllUsers() {
        return userService.getAllPresentUsers().stream().map(userMapper::fromUserForUser).collect(Collectors.toList());
    }

    public UserResponseOtherUserInfo anotherUserInfoById(int id) {
        return userMapper.fromUserForUser(userService.getUserById(id).orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found!")));
    }

    public UserResponse selfUserInfo(Principal principal) {
        return userMapper.fromUserForAdmin(userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login " + principal.getName() + " not found!")));
    }

    public UserResponse createUser(RequestUserRegistration userRegistration) {
        User user = userMapper.fromRequestUserRegistrationToUser(userRegistration);
        return userMapper.fromUserForAdmin(userService.createUser(user));
    }

     public UserResponse updateUser(RequestUserUpdate userUpdate, Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login " + principal.getName() + " not found!"));
        User updUser = userMapper.fromRequestUserUpdateToUser(user,userUpdate);
        return userMapper.fromUserForAdmin(userService.updateUser(updUser));
    }

    public void deleteUser(Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login " + principal.getName() + " not found!"));
   userService.deleteUser(user);
    }
}
