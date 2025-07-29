package com.example.personal_finance_tracker.security;


import com.example.personal_finance_tracker.entity.UserEntity;
import com.example.personal_finance_tracker.repo.UserEntityRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class TokenService {
    private final UserEntityRepository userRepository;

    public TokenService(UserEntityRepository userRepository) {
        this.userRepository = userRepository;
    }


    public String generateToken(UserEntity user) {

        Map<String, Object> clims = new HashMap<>();
        clims.put("id", user.getId());
        clims.put("username", user.getEmail());
        clims.put("roles", List.of());

        return Jwts.builder()
                .subject(user.getEmail())
                .claims(clims)
                .signWith(secretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60*24))
                .compact();
    }

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor("01234567890123456789012345678912".getBytes());
    }

   /* public Optional<UserEntity> getCurrentUser(String token) {
        if (validate(token)) {
            String username = getUserName(token);
            return userRepository.findByUsername(username);
        }
        return Optional.empty();
    }*/
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
   /* public List<SimpleGrantedAuthority> getRoles(String token) {
        Claims claims = getClaims(token);
        String roles = (String) claims.get("roles");
        return Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new).toList();
    }*/
}
