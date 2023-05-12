package com.tromza.pokertds.controller;

import com.tromza.pokertds.request.AuthRequest;
import com.tromza.pokertds.response.AuthResponse;
import com.tromza.pokertds.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest authRequest) {
        String token = authService.getTokenFromAuthRequest(authRequest);
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.CREATED);
    }
}


