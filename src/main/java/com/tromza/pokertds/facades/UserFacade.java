package com.tromza.pokertds.facades;

import com.tromza.pokertds.model.request.UserRegistrationRequest;
import com.tromza.pokertds.model.request.UserUpdateRequest;
import com.tromza.pokertds.model.response.UserResponse;
import com.tromza.pokertds.model.response.UserToOtherUserInfoResponse;

import java.security.Principal;
import java.util.List;

public interface UserFacade {
    List<UserToOtherUserInfoResponse> getAllUsers();

    UserToOtherUserInfoResponse anotherUserInfoById (int id);

    UserResponse selfUserInfo(Principal principal);

    UserResponse createUser (UserRegistrationRequest userRegistration);

    UserResponse updateUser (UserUpdateRequest userUpdate, Principal principal);

    void deleteUser(Principal principal);
}
