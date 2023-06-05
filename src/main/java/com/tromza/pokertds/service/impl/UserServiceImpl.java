package com.tromza.pokertds.service.impl;

import com.tromza.pokertds.model.domain.Game;
import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.repository.UserRepository;
import com.tromza.pokertds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public List<User> getAllPresentUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().filter(user -> !user.isDeleted()).filter(user -> user.getRole().equals("USER")).collect(Collectors.toList());
    }

    public List<User> getAllDeletedUsers() {
        return userRepository.findAllByIsDeletedTrue();
    }

    @Transactional
    public User createUser(User user) {
        if (getUserByLogin(user.getLogin()).isPresent()) {
            throw new UnsupportedOperationException("Such login is already used!");
        }
        if (getUserByEmail(user.getEmail()).isPresent()) {
            throw new UnsupportedOperationException("User with such email is already registered!");
        }
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    public void deleteUser(User user) {
        user.setDeleted(true);
        userRepository.saveAndFlush(user);
    }

    public void deleteUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found!"));
        if (user.isDeleted()) {
            throw new UnsupportedOperationException("User with id " + id + " is already deleted");
        } else {
            user.setDeleted(true);
            userRepository.saveAndFlush(user);
        }
    }

    public void cancelDeleteUserById (int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found!"));
        if (!user.isDeleted()) {
            throw new UnsupportedOperationException("User with id " + id + " is not deleted");
        } else {
            user.setDeleted(false);
            userRepository.saveAndFlush(user);
        }
    }

    public boolean isUserNotDeleted(String login) {
        return !getUserByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User with login " + login + " not found!")).isDeleted();
    }

    public void addGameToUser(User user, Game game) {
        userRepository.addGameToUser(game.getId(), user.getId());
    }

    public void saveUser(User user) {
        userRepository.saveAndFlush(user);
    }
}


