package com.tromza.pokertds.controller;

import com.tromza.pokertds.facades.AdminFacade;
import com.tromza.pokertds.model.request.UserMoneyAmountRequest;
import com.tromza.pokertds.model.response.UserResponse;
import com.tromza.pokertds.model.response.WalletResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Admin", description = "The Admin API")
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminFacade adminFacade;

    public AdminController(AdminFacade adminFacade) {
        this.adminFacade = adminFacade;
    }

    @Operation(summary = "Return list of actual users")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllPresentUsers() {
        return new ResponseEntity<>(adminFacade.getAllPresentUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Return list of deleted users")
    @GetMapping("/delusers")
    public ResponseEntity<List<UserResponse>> getAllDeletedUsers() {
        return new ResponseEntity<>(adminFacade.getAllDeletedUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Change isDeleted status of user with selected id")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUserByIdForAdmin(@PathVariable int id) {
        adminFacade.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Change isDeleted status of deleted user with selected id")
    @PutMapping("/delusers/{id}")
    public ResponseEntity<HttpStatus> cancelDeleteUserByIdForAdmin(@PathVariable int id) {
        adminFacade.cancelDeleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Withdraw or refill wallet of selected user")
    @PutMapping("/wallets")
    public ResponseEntity<WalletResponse> transferWallet(@RequestBody UserMoneyAmountRequest userMoney) {
        WalletResponse walletResponse = adminFacade.transferWallet(userMoney);
        return new ResponseEntity<>(walletResponse, HttpStatus.OK);
    }
}
