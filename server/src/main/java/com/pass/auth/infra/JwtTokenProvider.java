package com.pass.auth.infra;

import com.pass.auth.application.TokenProvider;
import com.pass.global.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements TokenProvider {

    private final SecretKey secretKey;
    private final Long expirationTime;

    public JwtTokenProvider(JwtTokenProperties properties) {
        this.secretKey = Keys.hmacShaKeyFor(properties.secretKey().getBytes(StandardCharsets.UTF_8));
        this.expirationTime = properties.expirationTime();
    }

    @Override
    public String createToken(String memberId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(memberId)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public Long getMemberId(String token) {
        Claims claims = toClaims(token);
        String memberId = claims.getSubject();

        return Long.parseLong(memberId);
    }

    private Claims toClaims(String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException("토큰이 존재하지 않습니다.");
        }

        try {
            Jws<Claims> claimsJws = getClaimsJws(token);

            return claimsJws.getPayload();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("토큰이 만료되었습니다.", e);
        } catch (JwtException e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.", e);
        }
    }

    private Jws<Claims> getClaimsJws(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }
}
