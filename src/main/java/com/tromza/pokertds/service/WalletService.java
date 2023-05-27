package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.Wallet;
import com.tromza.pokertds.request.UserMoneyAmount;

import java.security.Principal;
import java.util.Optional;

public interface WalletService {

    Optional<Wallet> getWalletByUserId(Integer userId);

    Optional<Wallet> getWalletForUser(Principal principal);

    Wallet createWallet(Integer userId);

    Wallet createWalletForPrincipal(Principal principal);

    Wallet updateWallet(UserMoneyAmount userMoney);

    Wallet refillWallet(UserMoneyAmount userMoney);

    Wallet withdrawWallet(UserMoneyAmount userMoney);
    }
