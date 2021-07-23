package com.example.reactchesslive.security;

import com.example.reactchesslive.security.auth.AuthenticationRequest;
import com.example.reactchesslive.security.auth.JWS;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;


@Component
public class JWTUtils {

    private static SecretKey key;

    private String keyEncoded;

    @PostConstruct
    void setSecretKey() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(keyEncoded));
    }

    public JWS createToken(AuthenticationRequest authenticationRequest) {
        Map<String, Object> claims = Map.of(Claims.EXPIRATION, Date.from(Instant.now().plusMillis(1000 * 60 * 60 * 12)),
                Claims.ISSUED_AT, Date.from(Instant.now()), Claims.SUBJECT, authenticationRequest.getUsername());

        String jwtString = Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new JWS(jwtString);
    }

    public Jws<Claims> parse(String token) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException | UnsupportedJwtException |
                MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return null;
        }
        return claimsJws;
    }

    @Value("${secretKey}")
    public void setKeyEncoded(String keyEncoded) {
        this.keyEncoded = keyEncoded;
    }
}
