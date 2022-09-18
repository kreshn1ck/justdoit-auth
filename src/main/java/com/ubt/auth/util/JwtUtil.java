package com.ubt.auth.util;

import com.ubt.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:JwtSecretKey}")
    private String secret;

    private static String SECRET_STATIC;

    @Value("${jwt.secret:JwtSecretKey}")
    public void setNameStatic(String secret) {
        JwtUtil.SECRET_STATIC = secret;
    }

    private static final long EXPIRATION_TIME = 1_200_000;

    public static String generateToken(User user) {
        Claims claims = JwtClaims.fromUserWithAccount(user);

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + EXPIRATION_TIME;
        Date expirationDate = new Date(expMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_STATIC)
                .compact();
    }

    /*public JwtClaims getAllClaimsFromToken(String token) {
        Claims claims = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                // .signWith(SignatureAlgorithm.HS512, secret)
                .
                .parseClaimsJws(token)
                .getBody();
        return new JwtClaims(claims);
    }

    public void validateToken(final String token) throws TokenValidationException {
        try {
            Jwts.parserBuilder().setSigningKey(secret.getBytes()).build().parseClaimsJws(token);
        } catch (MalformedJwtException ex) {
            throw new TokenValidationException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new TokenValidationException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new TokenValidationException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new TokenValidationException("JWT claims string is empty.");
        } catch (Exception e) {
            throw new TokenValidationException("Invalid exception.");
        }
    }*/
}
