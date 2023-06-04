package com.tromza.pokertds.facades;

import com.tromza.pokertds.model.response.WalletResponse;

import java.security.Principal;

public interface WalletFacade {

    WalletResponse getWalletForUser(Principal principal);

    WalletResponse createWalletForUser(Principal principal);
}
