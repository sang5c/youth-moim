package com.example.youthmoim.jwt;

import com.example.youthmoim.member.domain.RoleType;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    public static final int EXP_TIME_MS = 60 * 60 * 1000; // 60분
    private final SecretKey key;

    public boolean validate(String token) {
        try {
            Jws<Claims> claims = getJwtParser().parseClaimsJws(token);
            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            // TODO: log exception
            return false;
        }
    }

    public JwtPayload extractPayload(String token) {
        JwtParser jwtParser = getJwtParser();
        Claims claims = jwtParser.parseClaimsJws(token)
                .getBody();
        return JwtPayload.from(claims);
    }

    private JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

    public String generateToken(String username, List<RoleType> roles) {
        String role = roles.stream()
                .map(RoleType::name)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + EXP_TIME_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
