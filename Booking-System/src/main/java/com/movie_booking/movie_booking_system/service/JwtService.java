package com.movie_booking.movie_booking_system.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String generateToken(String username){
        Map<String, Object> claims=new HashMap<>();
        claims.put("role", "USER");

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5))
                .and()
                .signWith(getSignedKey())
                .compact();
    }

    private SecretKey getSignedKey(){
      byte[] keyBytes=this.SECRET_KEY.getBytes(StandardCharsets.UTF_8);
      return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token){
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
       try{
           Claims claims=parseClaims(token);
           return claims.getSubject().equals(userDetails.getUsername())
                   && claims.getExpiration().after(new Date());
       }
       catch (JwtException | IllegalArgumentException e){
           return false;
       }
    }

    private Claims parseClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}