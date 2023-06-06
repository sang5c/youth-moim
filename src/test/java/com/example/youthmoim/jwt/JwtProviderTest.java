package com.example.youthmoim.jwt;

import com.example.youthmoim.member.domain.RoleType;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderTest {
    private JwtProvider jwtProvider;

    @BeforeEach
    void setup() {
        byte[] keyBytes = Decoders.BASE64.decode("secret1q2w3e4r5t6y7u8i9o0pqwertyuiopasdfghjklzxcvbnm");
        this.jwtProvider = new JwtProvider(Keys.hmacShaKeyFor(keyBytes));
    }

    @Test
    void test() {
        String token = jwtProvider.generateToken("hello", List.of(RoleType.ORGANIZER, RoleType.PARTICIPANT));

        JwtPayload payload = jwtProvider.extractPayload(token);

        assertThat(payload.userId()).isEqualTo("hello");
        assertThat(payload.role()).isEqualTo("ORGANIZER,PARTICIPANT");
    }


}
