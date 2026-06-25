package com.authentication.jwt.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// In this clas we use static methods to use directly witout creating objects
@Component
public class JwtUtil{

    @Value("${jwt.secret}")
    private String secret;
  
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }



    // public static final String SECRET = "mysecretkeymysecretkeymysecretkey123";

    // public static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration( new Date ( System.currentTimeMillis() + 3600000 )) // Expire 1 hour
                .signWith(getKey())
                .compact();
    }


    // This checks: Signature, Expiration, Token format
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (Exception e) {
            return false;
        }
    }



    // Get username from token
    public String extractUsername(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }
    

}