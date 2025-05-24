package br.com.victorpizzaia.wallet_service_assignment.auth.domain;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    @Value("${security.jwt.encryption_key}")
    private String encryptionKey;
    @Value("${security.jwt.expiration_time}")
    private long expirationTime;

    private Key generateSecurityKey() {
        byte[] base64Key = Decoders.BASE64.decode(encryptionKey);
        return Keys.hmacShaKeyFor(base64Key);
    }

    public String generateToken(String userId, String identifier) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("identifier", identifier)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(generateSecurityKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(generateSecurityKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserIdFromJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateSecurityKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public long getExpiresIn() {
        return expirationTime / 1000;
    }
}
