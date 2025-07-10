package com.playvoice.userservice.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.playvoice.userservice.entity.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    private final long tokenValidTime = 1000L * 60 * 60;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(String username, Long userId, UserRole role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("userId", userId);
        claims.put("role", role.name());

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUSername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRole(String token) {
        return (String) Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String createAccessToken(String username, Long userId, UserRole role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 15 * 60 * 1000); // 15minutes

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String createRefreshToken(String username, Long userId, UserRole role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 60 * 60 * 1000); // 1hour

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public UserRole extractRole(String token) {
        return UserRole.valueOf((String) getClaims(token).get("role"));
    }

    public Long extractUserId(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

}
