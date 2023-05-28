package com.tromza.pokertds.facades;

import com.tromza.pokertds.response.WalletResponse;

import java.security.Principal;

public interface WalletFacade {

    WalletResponse getWalletForUser(Principal principal);

    WalletResponse createWalletForUser(Principal principal);
}
