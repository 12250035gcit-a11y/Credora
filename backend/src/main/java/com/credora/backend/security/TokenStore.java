package com.credora.backend.security;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Keeps logged-in sessions in memory: token -> userId.
 *
 * This project uses a plain random token instead of JWT or full Spring Security
 * on purpose - it is a lot less code to read and explain, and is enough for a
 * single-instance deployment. See the documentation for the trade-off.
 */
@Component
public class TokenStore {

    private final Map<String, Long> tokenToUserId = new ConcurrentHashMap<>();

    public String issueToken(Long userId) {
        String token = UUID.randomUUID().toString();
        tokenToUserId.put(token, userId);
        return token;
    }

    public Long resolveUserId(String token) {
        return tokenToUserId.get(token);
    }

    public void revoke(String token) {
        tokenToUserId.remove(token);
    }
}
