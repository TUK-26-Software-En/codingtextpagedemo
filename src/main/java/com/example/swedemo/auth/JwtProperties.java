package com.example.swedemo.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT 설정 바인딩. jwt.secret(서명 키, 운영은 env 주입), jwt.expiration-ms(만료).
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;

    private long expirationMs = 3_600_000L;
}
