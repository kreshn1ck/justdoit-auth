package com.ubt.auth.util;

import com.ubt.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtClaims {

    private static final String ID = "id";
    private static final String EMAIL = "eml";
    private static final String USERNAME = "usr";

    static final long EXPIRATION_TIME = 1_200_000;

    private final Claims claims;

    JwtClaims(Claims claims) {
        this.claims = claims;
    }

    public Long getUserId() {
        return claims.get(ID, Long.class);
    }

    public String getUserEmail() {
        return claims.get(EMAIL, String.class);
    }

    public String getUsername() {
        return claims.get(USERNAME, String.class);
    }

    static Claims fromUserWithAccount(User user) {
        Claims claims = Jwts.claims();
        claims.put(EMAIL, user.getEmail());
        claims.put(ID, user.getId());
        claims.put(USERNAME, user.getUsername());
        return claims;
    }

}
