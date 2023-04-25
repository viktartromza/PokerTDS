package com.tromza.pokertds.controller;

import com.tromza.pokertds.domain.Wallet;
import com.tromza.pokertds.request.UserMoneyAmount;
import com.tromza.pokertds.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    Logger log = LoggerFactory.getLogger(this.getClass());

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/info")
    public ResponseEntity<Wallet> getWalletForUser(Principal principal) {
        Optional<Wallet> wallet = walletService.getWalletForUser(principal);
        return wallet.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/")
    public ResponseEntity<Wallet> createWalletForUser(Principal principal){

            return new ResponseEntity<>(walletService.createWalletForPrincipal(principal), HttpStatus.CREATED);

    }
    @PutMapping("/")
    public ResponseEntity<Wallet> transferWallet(@RequestBody UserMoneyAmount userMoney) {
        Wallet wallet = walletService.updateWallet(userMoney);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }
}