package com.tromza.pokertds.controller;

import com.tromza.pokertds.domain.Wallet;
import com.tromza.pokertds.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Tag(name="Wallet", description="The Wallet API")
@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Operation(summary = "Get info about wallet balance of current user")
    @GetMapping("/info")
    public ResponseEntity<Wallet> getWalletForUser(Principal principal) {
        Optional<Wallet> wallet = walletService.getWalletForUser(principal);
        return wallet.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create wallet for current user")
    @PostMapping
    public ResponseEntity<Wallet> createWalletForUser(Principal principal) {
        return new ResponseEntity<>(walletService.createWalletForPrincipal(principal), HttpStatus.CREATED);
    }
}
