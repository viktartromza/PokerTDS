package com.tromza.pokertds.controller;

import com.tromza.pokertds.facades.AuthFacade;
import com.tromza.pokertds.model.request.AuthRequest;
import com.tromza.pokertds.model.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "The Authentication API")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthFacade authFacade;

    public AuthController(AuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    @Operation(summary = "Return a token for user")
    @PostMapping
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authFacade.getTokenForUser(authRequest), HttpStatus.CREATED);
    }
}