package com.project.task_manager.util;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class SecurityConstants {
    @Value("${REMOVED}")
    private String SECRET_KEY;
    @Value("${REMOVED}")
    protected long EXPIRATION_TIME;

    protected Key KEY;

    @PostConstruct
    public void init() {
        this.KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public long getEXPIRATION_TIME() {
        return EXPIRATION_TIME;
    }

    public Key getKEY() {
        return KEY;
    }
}
