package com.tromza.pokertds.facades.impl;

import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.model.domain.Wallet;
import com.tromza.pokertds.facades.AdminFacade;
import com.tromza.pokertds.mapper.UserMapper;
import com.tromza.pokertds.mapper.WalletMapper;
import com.tromza.pokertds.model.request.UserMoneyAmountRequest;
import com.tromza.pokertds.model.response.UserResponse;
import com.tromza.pokertds.model.response.WalletResponse;
import com.tromza.pokertds.service.UserService;
import com.tromza.pokertds.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    @Transactional
    public WalletResponse transferWallet(UserMoneyAmountRequest userMoneyAmountRequest) {
        User user = userService.getUserById(userMoneyAmountRequest.getUserId()).orElseThrow(() -> new NoSuchElementException("User with id: " + userMoneyAmountRequest.getUserId() + " not found!"));
        Optional<Wallet> optionalWallet = walletService.getWalletForUser(user);
        Wallet wallet = optionalWallet.orElseGet(() -> walletService.createWalletForUser(user));
        BigDecimal amount = userMoneyAmountRequest.getAmount();
        return walletMapper.walletResponse(walletService.updateWallet(wallet, amount));
    }
}
