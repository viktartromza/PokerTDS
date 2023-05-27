package com.tromza.pokertds.facades;

import com.tromza.pokertds.request.UserMoneyAmount;
import com.tromza.pokertds.response.UserResponse;
import com.tromza.pokertds.response.WalletResponse;

import java.util.List;

public interface AdminFacade {

    List<UserResponse> getAllPresentUsers();

    List<UserResponse> getAllDeletedUsers();

    void deleteUserById(int id);

    WalletResponse transferWallet(UserMoneyAmount userMoneyAmount);
}
