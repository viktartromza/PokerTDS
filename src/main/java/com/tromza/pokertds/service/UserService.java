package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.request.RequestUserRegistration;
import com.tromza.pokertds.request.RequestUserUpdate;
import com.tromza.pokertds.response.ResponseOtherUserInfo;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<ResponseOtherUserInfo> otherUserInfo(int id);

    Optional<User> getUserByLogin(String login);

    Optional<User> getUserByEmail(String email);

    List<User> getAllPresentUsers();

    List<User> getAllDeletedUsers();

    User createUser(RequestUserRegistration userRegistration);

    User updateUser(RequestUserUpdate requestUserUpdate, Principal principal);

    void deleteUser(Principal principal);

    void deleteUserById(int id);

    boolean isUserNotDeleted(String login);

    void addGameToUser(User user, Game game);

    public void saveUser(User user);
}
