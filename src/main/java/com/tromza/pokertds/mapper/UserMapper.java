package com.tromza.pokertds.mapper;

import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.model.request.UserRegistrationRequest;
import com.tromza.pokertds.model.request.UserUpdateRequest;
import com.tromza.pokertds.model.response.UserResponse;
import com.tromza.pokertds.model.response.UserToOtherUserInfoResponse;
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

    public UserToOtherUserInfoResponse fromUserForUser(User user) {
        UserToOtherUserInfoResponse userResponse = new UserToOtherUserInfoResponse();
        userResponse.setId(user.getId());
        userResponse.setLogin(user.getLogin());
        userResponse.setScore(user.getScore());
        return userResponse;
    }

    public User fromRequestUserRegistrationToUser(UserRegistrationRequest userRegistration) {
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

    public User fromRequestUserUpdateToUser(User user, UserUpdateRequest userUpdate) {
        user.setChanged(new Timestamp(System.currentTimeMillis()));
        user.setFirstName(userUpdate.getFirstName());
        user.setLastName(userUpdate.getLastName());
        user.setCountry(userUpdate.getCountry());
        user.setBirthDay(userUpdate.getBirthDay());
        user.setTelephone(userUpdate.getTelephone());
        return user;
    }
}
