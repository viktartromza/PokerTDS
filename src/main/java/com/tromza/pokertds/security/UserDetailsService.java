package com.tromza.pokertds.security;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.nio.file.attribute.UserPrincipalNotFoundException;


@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findUserByLogin(login).orElseThrow(() -> new UsernameNotFoundException("User with login " + login + " not found!"));
        if (user.isDeleted()) {
            throw new UsernameNotFoundException("User with login " + login + " is deleted!");
        } else {
            UserDetails securityUser = org.springframework.security.core.userdetails.User
                    .builder()
                    .username(user.getLogin())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
            return securityUser;
        }
    }
}
