package org.example.catch_line.user.auth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String userName) {
        String token = Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, decodeSecret(secret))
                .compact();
        log.info("generated JWT Token: {}", token);
        return token;

    }

    private byte[] decodeSecret(String secret) {
        return Base64.getDecoder().decode(secret);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Boolean validateToken(String token, String userName) {
        final String emailFromToken = getUsernameFromToken(token);
        return (Objects.equals(emailFromToken, userName) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(decodeSecret(secret)) // 디코딩된 secret 사용
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}