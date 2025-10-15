package com.uni_store.store.Security;

import io.jsonwebtoken.Claims;

import java.util.Date;

public record Jwt(
        Claims claims
) {
    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public String getUsername() {
        return claims.getSubject();
    }
}
