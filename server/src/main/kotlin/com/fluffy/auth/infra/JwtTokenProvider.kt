package com.fluffy.auth.infra

import com.fluffy.auth.application.TokenProvider
import com.fluffy.global.exception.UnauthorizedException
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class JwtTokenProvider(
    properties: JwtTokenProperties,
) : TokenProvider {

    private val secretKey = Keys.hmacShaKeyFor(properties.secretKey.toByteArray(StandardCharsets.UTF_8))
    private val expirationTime = properties.expirationTime

    override fun createToken(memberId: String): String {
        val now = Date()
        val expiration = Date(now.time + expirationTime)

        return Jwts.builder()
            .subject(memberId)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(secretKey)
            .compact()
    }

    override fun getMemberId(token: String): Long {
        val claims = toClaims(token)
        val memberId = claims.subject

        return memberId.toLong()
    }

    private fun toClaims(token: String): Claims {
        if (token.isBlank()) {
            throw UnauthorizedException("토큰이 존재하지 않습니다.")
        }

        try {
            val claimsJws = getClaimsJws(token)

            return claimsJws.payload
        } catch (e: ExpiredJwtException) {
            throw UnauthorizedException("토큰이 만료되었습니다.", e)
        } catch (e: JwtException) {
            throw UnauthorizedException("유효하지 않은 토큰입니다.", e)
        }
    }

    private fun getClaimsJws(token: String): Jws<Claims> {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
    }
}