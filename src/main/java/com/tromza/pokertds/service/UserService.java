package com.tromza.pokertds.service;

import com.tromza.pokertds.model.domain.Game;
import com.tromza.pokertds.model.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(int id);

    Optional<User> getUserByLogin(String login);

    Optional<User> getUserByEmail(String email);

    List<User> getAllPresentUsers();

    List<User> getAllDeletedUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    void deleteUserById(int id);

    void cancelDeleteUserById(int id);

    boolean isUserNotDeleted(String login);

    void addGameToUser(User user, Game game);

    void saveUser(User user);
}
