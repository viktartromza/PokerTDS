package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.domain.Wallet;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletService {

    Optional<Wallet> getWalletByUserId(Integer userId);

    Optional<Wallet> getWalletForUser(User user);

    Wallet createWallet(Integer userId);

    Wallet createWalletForUser(User user);

    Wallet updateWallet(Wallet wallet, BigDecimal amount);

    Wallet refillWallet(Wallet wallet, BigDecimal amount);

    Wallet withdrawWallet(Wallet wallet, BigDecimal amount);
    }
