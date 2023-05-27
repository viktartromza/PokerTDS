package com.tromza.pokertds.security;

import com.tromza.pokertds.service.impl.UserServiceImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public JwtService(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Value("${jwt-expire-time}")
    private Integer expireTime;

    @Value("${jwt-key}")
    private String key;

    public String createJwtToken(String login) {
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(expireTime)))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String getLoginFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Boolean isValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            if (userServiceImpl.isUserNotDeleted(getLoginFromToken(token))) {
                return true;
            }
        } catch (JwtException e) {
            log.warn(e.getClass() + " : " + e.getMessage());
        }
        return false;
    }
}
