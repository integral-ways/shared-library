package com.itways.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
public class JwtSharedConfig {

    @Value("${security.jwt.secret-key}")
    private String jwtSecret; // Store this in application.properties or env


    @Bean
    public Key jwtSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Bean
    public JwtValidationService jwtValidationService(Key signingKey) {
        return new JwtValidationService(signingKey);
    }

}
