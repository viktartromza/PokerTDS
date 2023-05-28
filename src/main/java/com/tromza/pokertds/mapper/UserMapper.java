package com.tromza.pokertds.mapper;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.request.RequestUserRegistration;
import com.tromza.pokertds.request.RequestUserUpdate;
import com.tromza.pokertds.response.UserResponse;
import com.tromza.pokertds.response.UserResponseOtherUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;

@Component
public class UserMapper {

    @Value("${defaultRole}")
    private String ROLE;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse fromUserForAdmin(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setLogin(user.getLogin());
        userResponse.setRegDate(user.getRegDate());
        userResponse.setEmail(user.getEmail());
        userResponse.setScore(user.getScore());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCountry(user.getCountry());
        userResponse.setBirthDay(user.getBirthDay());
        userResponse.setTelephone(user.getTelephone());
        userResponse.setChanged(user.getChanged());
        return userResponse;
    }

    public UserResponseOtherUserInfo fromUserForUser(User user) {
        UserResponseOtherUserInfo userResponse = new UserResponseOtherUserInfo();
        userResponse.setId(user.getId());
        userResponse.setLogin(user.getLogin());
        userResponse.setScore(user.getScore());
        return userResponse;
    }

    public User fromRequestUserRegistrationToUser(RequestUserRegistration userRegistration) {
        User user = new User();
        user.setLogin(userRegistration.getLogin());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        user.setEmail(userRegistration.getEmail());
        user.setRegDate(new Date(System.currentTimeMillis()));
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        user.setDeleted(false);
        user.setRole(ROLE);
        return user;
    }

    public User fromRequestUserUpdateToUser(User user, RequestUserUpdate userUpdate) {
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        user.setFirstName(userUpdate.getFirstName());
        user.setLastName(userUpdate.getLastName());
        user.setCountry(userUpdate.getCountry());
        user.setBirthDay(userUpdate.getBirthDay());
        user.setTelephone(userUpdate.getTelephone());
        return user;
    }
}
