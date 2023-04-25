package com.tromza.pokertds.service;


import com.tromza.pokertds.domain.Game;
import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.repository.UserRepository;
import com.tromza.pokertds.request.RequestUserRegistration;
import com.tromza.pokertds.request.RequestUserUpdate;
import com.tromza.pokertds.response.ResponseOtherUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.security.Principal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${defaultRole}")
    private String ROLE;

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

    public List<ResponseOtherUserInfo> getAllUsersForUser() {
        List<User> users = userRepository.findAll();
        return users.stream().filter(user -> !user.isDeleted()).map(user->new ResponseOtherUserInfo(user.getId(), user.getLogin(), user.getScore())).collect(Collectors.toList());
    }

    @Transactional
    public User createUser(RequestUserRegistration userRegistration) {
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

    @Transactional
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

    public void deleteUser (Principal principal){
        User user = getUserByLogin(principal.getName()).orElseThrow(()->new UsernameNotFoundException("User with login " + principal.getName() + " not found!"));
        user.setDeleted(true);
        userRepository.saveAndFlush(user);
    }

    public void addGameToUser(User user, Game game) {
        userRepository.addGameToUser(game.getId(), user.getId());
    }

    public void saveUser(User user) {
        userRepository.saveAndFlush(user);
    }
}


