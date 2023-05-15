package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.repository.UserRepository;
import com.tromza.pokertds.request.RequestUserRegistration;
import com.tromza.pokertds.request.RequestUserUpdate;
import com.tromza.pokertds.response.ResponseOtherUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${defaultRole}")
    private String ROLE;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<ResponseOtherUserInfo> otherUserInfo(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> new ResponseOtherUserInfo(value.getId(), value.getLogin(), value.getScore()));
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public List<User> getAllPresentUsersForAdmin() {
        List<User> users = userRepository.findAll();
        return users.stream().filter(user -> !user.isDeleted()).filter(user -> user.getRole().equals("USER")).collect(Collectors.toList());
    }

    public List<User> getAllDeletedUsersForAdmin() {
        return userRepository.findAllByIsDeletedTrue();
    }

    public List<ResponseOtherUserInfo> getAllUsersForUser() {
        List<User> users = userRepository.findAll();
        return users.stream().filter(user -> !user.isDeleted()).filter(user -> user.getRole().equals("USER")).map(user -> new ResponseOtherUserInfo(user.getId(), user.getLogin(), user.getScore())).collect(Collectors.toList());
    }

    @Transactional
    public User createUser(RequestUserRegistration userRegistration) {
        if (getUserByLogin(userRegistration.getLogin()).isPresent()) {
            throw new UnsupportedOperationException("Such login is already used!");
        }
        if (getUserByEmail(userRegistration.getEmail()).isPresent()) {
            throw new UnsupportedOperationException("User with such email is already registered!");
        }
        User user = new User();
        user.setLogin(userRegistration.getLogin());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        user.setEmail(userRegistration.getEmail());
        user.setRegDate(new Date(System.currentTimeMillis()));
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        user.setDeleted(false);
        user.setRole(ROLE);
        return userRepository.save(user);
    }

    public User updateUser(RequestUserUpdate requestUserUpdate, Principal principal) {
        User user = getUserByLogin(principal.getName()).get();
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        user.setFirstName(requestUserUpdate.getFirstName());
        user.setLastName(requestUserUpdate.getLastName());
        user.setCountry(requestUserUpdate.getCountry());
        user.setBirthDay(requestUserUpdate.getBirthDay());
        user.setTelephone(requestUserUpdate.getTelephone());
        return userRepository.saveAndFlush(user);
    }

    public void deleteUser(Principal principal) {
        User user = getUserByLogin(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User with login " + principal.getName() + " not found!"));
        user.setDeleted(true);
        userRepository.saveAndFlush(user);
    }

    public void deleteUserByIdForAdmin(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found!"));
        if (user.isDeleted()) {
            throw new UnsupportedOperationException("User with id " + id + " is already deleted");
        } else {
            user.setDeleted(true);
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


