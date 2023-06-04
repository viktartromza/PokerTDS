package com.tromza.pokertds.facades.impl;

import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.facades.WalletFacade;
import com.tromza.pokertds.mapper.WalletMapper;
import com.tromza.pokertds.model.response.WalletResponse;
import com.tromza.pokertds.service.UserService;
import com.tromza.pokertds.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.NoSuchElementException;
@Service
public class WalletFacadeImpl implements WalletFacade {

    private final WalletService walletService;
    private final UserService userService;
    private final WalletMapper walletMapper;

    @Autowired
    public WalletFacadeImpl(WalletService walletService, UserService userService, WalletMapper walletMapper) {
        this.walletService = walletService;
        this.userService = userService;
        this.walletMapper = walletMapper;
    }

    public WalletResponse getWalletForUser(Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login " + principal.getName() + " not found!"));
        return walletMapper.walletResponse(walletService.getWalletForUser(user).orElseThrow(() -> new NoSuchElementException("User with login " + user.getLogin() + " haven't wallet!")));
    }

    public WalletResponse createWalletForUser(Principal principal) {
        User user = userService.getUserByLogin(principal.getName()).orElseThrow(() -> new NoSuchElementException("User with login " + principal.getName() + " not found!"));
        return walletMapper.walletResponse(walletService.createWalletForUser(user));
    }
}
