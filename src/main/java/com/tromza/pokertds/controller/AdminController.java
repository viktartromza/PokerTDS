package com.tromza.pokertds.controller;


import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.domain.Wallet;
import com.tromza.pokertds.request.UserMoneyAmount;
import com.tromza.pokertds.service.UserService;
import com.tromza.pokertds.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="Admin", description="The Admin API")
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

    @Operation(summary = "Return list of actual users")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllPresentUsers() {
        return new ResponseEntity<>(userService.getAllPresentUsersForAdmin(), HttpStatus.OK);
    }

    @Operation(summary = "Return list of deleted users")
    @GetMapping("/delusers")
    public ResponseEntity<List<User>> getAllDeletedUsers() {
        return new ResponseEntity<>(userService.getAllDeletedUsersForAdmin(), HttpStatus.OK);
    }

    @Operation(summary = "Change isDeleted status of user with selected id")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUserByIdForAdmin(@PathVariable int id) {
        userService.deleteUserByIdForAdmin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Withdraw or refill wallet of selected user")
    @PutMapping("/wallets")
    public ResponseEntity<Wallet> transferWallet(@RequestBody UserMoneyAmount userMoney) {
        Wallet wallet = walletService.updateWallet(userMoney);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }
}
