package com.tromza.pokertds.facades.impl;

import com.tromza.pokertds.facades.AdminFacade;
import com.tromza.pokertds.mapper.UserMapper;
import com.tromza.pokertds.mapper.WalletMapper;
import com.tromza.pokertds.request.UserMoneyAmount;
import com.tromza.pokertds.response.UserResponse;
import com.tromza.pokertds.response.WalletResponse;
import com.tromza.pokertds.service.UserService;
import com.tromza.pokertds.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminFacadeImpl implements AdminFacade {

    private final UserService userService;
    private final WalletService walletService;
    private final UserMapper userMapper;
    private final WalletMapper walletMapper;

@Autowired
    public AdminFacadeImpl(UserService userService, WalletService walletService, UserMapper userMapper, WalletMapper walletMapper) {
        this.userService = userService;
        this.walletService = walletService;
        this.userMapper = userMapper;
        this.walletMapper = walletMapper;
    }

    public List<UserResponse> getAllPresentUsers() {
        List<UserResponse> users = userService.getAllPresentUsers().stream().map(userMapper::fromUserForAdmin).toList();
        return users;
    }

    public List<UserResponse> getAllDeletedUsers() {
        List<UserResponse> deletedUsers = userService.getAllDeletedUsers().stream().map(userMapper::fromUserForAdmin).toList();
        return deletedUsers;
    }

       public void deleteUserById(int id) {
        userService.deleteUserById(id);
    }

    public WalletResponse transferWallet(UserMoneyAmount userMoneyAmount) {
        return walletMapper.walletResponse(walletService.updateWallet(userMoneyAmount));
    }
}
