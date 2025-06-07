package org.goongut.configuration

import org.goongut.security.JwtUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    @Bean
    fun jwtUtil(): JwtUtil = JwtUtil(secretKey)
}
