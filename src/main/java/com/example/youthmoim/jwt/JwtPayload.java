package com.example.youthmoim.jwt;

import io.jsonwebtoken.Claims;

public record JwtPayload(String userId, String role) {
    public static JwtPayload from(Claims claims) {
        return new JwtPayload(
                claims.getSubject(),
                claims.get("role", String.class)
        );
    }
}
