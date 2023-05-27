package com.tromza.pokertds.security;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public JwtFilter(JwtService jwtService, UserServiceImpl userServiceImpl) {
        this.jwtService = jwtService;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String prefix = "Bearer ";
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(prefix)) {
            String token = header.substring(prefix.length());
            if (jwtService.isValid(token)) {
                User user = userServiceImpl.getUserByLogin(jwtService.getLoginFromToken(token)).orElseThrow(() -> new UsernameNotFoundException("User with login " + jwtService.getLoginFromToken(token) + " not found!"));
                UserDetails securityUser = org.springframework.security.core.userdetails.User
                        .builder()
                        .username(user.getLogin())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
