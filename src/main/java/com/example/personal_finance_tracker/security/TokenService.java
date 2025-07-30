package com.example.personal_finance_tracker.security;

import com.example.personal_finance_tracker.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class TokenService {

    public String generateToken(UserEntity user) {
        Map<String, Object> clims = new HashMap<>();
        clims.put("id", user.getId());
        clims.put("username", user.getEmail());
        return Jwts.builder()
                .subject(user.getEmail())
                .claims(clims)
                .signWith(secretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .compact();
    }

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor("01234567890123456789012345678912".getBytes());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validate(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserName(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }
}
