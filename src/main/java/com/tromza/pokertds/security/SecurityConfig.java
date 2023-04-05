package com.tromza.pokertds.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
public void registerGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth
            .inMemoryAuthentication()
            .withUser("admin")
            .password((EncoderConfig.passwordEncoder().encode("admin")))
            .roles("ADMIN");

   /* auth
            .inMemoryAuthentication()
            .withUser("user")
            .password(passwordEncoder().encode("user"))
            .authorities("ROLE_USER");*/
}
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/securityNone")
                .permitAll()
                .antMatchers(HttpMethod.DELETE,"user/**")
                .permitAll()
                .antMatchers(HttpMethod.PUT,"/*")
                .permitAll()
                .antMatchers(HttpMethod.POST,"/*")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and().build();
    }
   }

