package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.request.AuthRequest;
import com.tromza.pokertds.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public String getTokenFromAuthRequest(AuthRequest authRequest) {
        User user = userService.getUserByLogin(authRequest.getLogin()).orElseThrow(() -> new UsernameNotFoundException("User with login " + authRequest.getLogin() + " not found!"));
        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())&& !user.isDeleted()) {
            return jwtService.createJwtToken(authRequest.getLogin());
        } else {
throw new BadCredentialsException("Login or password is incorrect");
        }
    }
}
