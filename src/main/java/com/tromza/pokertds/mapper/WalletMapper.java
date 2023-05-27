package com.tromza.pokertds.mapper;

import com.tromza.pokertds.domain.Wallet;
import com.tromza.pokertds.response.WalletResponse;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletResponse walletResponse(Wallet wallet) {
        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setBalance(wallet.getBalance());
        walletResponse.setUserId(walletResponse.getUserId());
        return walletResponse;
    }
}
