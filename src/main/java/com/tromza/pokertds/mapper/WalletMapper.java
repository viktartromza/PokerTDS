package com.tromza.pokertds.mapper;

import com.tromza.pokertds.model.domain.Wallet;
import com.tromza.pokertds.model.response.WalletResponse;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletResponse walletResponse(Wallet wallet) {
        WalletResponse walletResponse = new WalletResponse();
        walletResponse.setBalance(wallet.getBalance());
        walletResponse.setUserId(wallet.getUserId());
        return walletResponse;
    }
}
