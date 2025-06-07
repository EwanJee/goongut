@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.goongut.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.goongut.domain.error.CustomException
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec

class JwtUtil(
    private val secretKey: String,
) {
    companion object {
        private const val EXPIRATION_TIME = 60 * 60 * 1000 * 4 // 4시간
    }

    fun generateToken(
        subject: String,
        claims: Map<String, Any>,
    ): String {
        val signingKey: Key = SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS256.jcaName)
        return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(subject)
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun createAccessToken(
        email: String,
        role: String,
    ): String =
        Jwts
            .builder()
            .setSubject(email)
            .claim("role", role)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1시간
            .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS256.jcaName))
            .compact()

    fun parseClaims(jwt: String): MutableMap<String, Any> {
        val signingKey: Key = SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS256.jcaName)
        val claims =
            Jwts
                .parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(jwt)
                .body
        if (claims.expiration.before(Date())) {
            throw CustomException("토큰이 만료되었습니다.")
        }
        if (claims.size == 0) {
            throw CustomException("토큰이 유효하지 않습니다.")
        }
        return claims
    }
}
