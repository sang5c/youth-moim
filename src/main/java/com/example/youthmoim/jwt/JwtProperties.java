package com.example.youthmoim.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret) {

    @ConstructorBinding
    public JwtProperties {
    }
}
