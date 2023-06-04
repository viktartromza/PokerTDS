package com.tromza.pokertds.facades;

import com.tromza.pokertds.model.request.UserMoneyAmountRequest;
import com.tromza.pokertds.model.response.UserResponse;
import com.tromza.pokertds.model.response.WalletResponse;

import java.util.List;

public interface AdminFacade {

    List<UserResponse> getAllPresentUsers();

    List<UserResponse> getAllDeletedUsers();

    void deleteUserById(int id);

    WalletResponse transferWallet(UserMoneyAmountRequest userMoneyAmountRequest);
}
