package com.tromza.pokertds.mapper;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse fromUserForAdmin (User user)
    {
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
}
