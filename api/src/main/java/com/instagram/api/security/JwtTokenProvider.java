package com.instagram.api.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.instagram.api.config.SecurityContext;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtTokenProvider {
    public JwtTokenClaims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SecurityContext.JWT_KEY.getBytes());
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        String username = String.valueOf(claims.get("username"));
        JwtTokenClaims jwtTokenClaims = new JwtTokenClaims();
        jwtTokenClaims.setUsername(username);
        return jwtTokenClaims;
    }
    // Phương thức để tạo JWT token
    public String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(SecurityContext.JWT_KEY.getBytes());
        return Jwts.builder()
                .setSubject(username) // Đặt username là subject
                .setIssuedAt(new Date()) // Thời gian phát hành
                .setExpiration(new Date(System.currentTimeMillis() + 300000000 * 6)) // Thời gian hết hạn (ví dụ: 5 phút)
                .signWith(key) // Ký token
                .compact(); // Trả về token
    }
}
