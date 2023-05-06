package com.tromza.pokertds.controller;


import com.tromza.pokertds.domain.Wallet;
import com.tromza.pokertds.request.UserMoneyAmount;
import com.tromza.pokertds.service.UserService;
import com.tromza.pokertds.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final WalletService walletService;
@Autowired
    public AdminController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUserByIdForAdmin(@PathVariable int id) {
        userService.deleteUserByIdForAdmin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/wallets")
    public ResponseEntity<Wallet> transferWallet(@RequestBody UserMoneyAmount userMoney) {
        Wallet wallet = walletService.updateWallet(userMoney);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }
}
