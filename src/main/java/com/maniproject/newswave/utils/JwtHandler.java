package com.maniproject.newswave.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtHandler {

    private static final long TIME_ONE_MINUTE = 1000 * 60;
    private static final long TIME_LONG_ONE_YEAR = 1000L * 60 * 60 * 24 * 365;
    private String jwtSecret;

    public JwtHandler( @Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public String generateVerificationToken(String email, long numMinutes) {
        Date expirationDate = new Date(System.currentTimeMillis() + numMinutes * TIME_ONE_MINUTE);
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateVerificationTokenLong(String email) {
        return generateVerificationToken(email, TIME_LONG_ONE_YEAR/ TIME_ONE_MINUTE);
    }

    public String parseJwt(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
    public boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}
