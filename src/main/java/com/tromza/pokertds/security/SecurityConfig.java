package com.tromza.pokertds.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
private final UserDetailsService userDetailsService;
@Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.POST,"/auth").permitAll()
                .antMatchers(HttpMethod.POST,"/users/registration").permitAll()
                .antMatchers(HttpMethod.GET, "users/info").hasRole("USER")
                .antMatchers(HttpMethod.GET, "users/{id}").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "users/update").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "users/").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/*").permitAll()//TODO permissions
                .antMatchers(HttpMethod.POST, "/*").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().userDetailsService(userDetailsService)
                .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/auth"))
                .and().httpBasic()
                .and().build();
    }
}

