package com.tromza.pokertds.controller;

import com.tromza.pokertds.facades.WalletFacade;
import com.tromza.pokertds.model.response.WalletResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name="Wallet", description="The Wallet API")
@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletFacade walletFacade;

    public WalletController(WalletFacade walletFacade) {
        this.walletFacade = walletFacade;
    }

    @Operation(summary = "Get info about wallet balance of current user")
    @GetMapping("/info")
    public ResponseEntity<WalletResponse> getWalletForUser(Principal principal) {
        WalletResponse wallet = walletFacade.getWalletForUser(principal);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @Operation(summary = "Create wallet for current user")
    @PostMapping
    public ResponseEntity<WalletResponse> createWalletForUser(Principal principal) {
        return new ResponseEntity<>(walletFacade.createWalletForUser(principal), HttpStatus.CREATED);
    }
}
