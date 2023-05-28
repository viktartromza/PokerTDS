package com.tromza.pokertds.facades;

import com.tromza.pokertds.request.RequestUserRegistration;
import com.tromza.pokertds.request.RequestUserUpdate;
import com.tromza.pokertds.response.UserResponse;
import com.tromza.pokertds.response.UserResponseOtherUserInfo;

import java.security.Principal;
import java.util.List;

public interface UserFacade {
    List<UserResponseOtherUserInfo> getAllUsers();

    UserResponseOtherUserInfo anotherUserInfoById (int id);

    UserResponse selfUserInfo(Principal principal);

    UserResponse createUser (RequestUserRegistration userRegistration);

    UserResponse updateUser (RequestUserUpdate userUpdate, Principal principal);

    void deleteUser(Principal principal);
}
