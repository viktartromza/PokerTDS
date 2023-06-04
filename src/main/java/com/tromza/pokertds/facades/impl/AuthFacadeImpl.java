package com.tromza.pokertds.facades.impl;

import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.facades.AuthFacade;
import com.tromza.pokertds.model.request.AuthRequest;
import com.tromza.pokertds.model.response.AuthResponse;
import com.tromza.pokertds.security.JwtService;

import com.tromza.pokertds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthFacadeImpl implements AuthFacade {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthFacadeImpl(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse getTokenForUser(AuthRequest authRequest) {
        User user = userService.getUserByLogin(authRequest.getLogin()).orElseThrow(() -> new UsernameNotFoundException("User with login " + authRequest.getLogin() + " not found!"));
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword()) && !user.isDeleted()) {
            return new AuthResponse(jwtService.createJwtToken(authRequest.getLogin()));
        } else {
            throw new BadCredentialsException("Login or password is incorrect");
        }
    }
}

