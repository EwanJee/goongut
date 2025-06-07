@file:Suppress("ktlint:standard:filename")

package org.goongut.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ForwardedHeaderFilter

@Configuration
class SwaggerConfig {
    @Bean
    fun forwardHeadFilter(): ForwardedHeaderFilter = ForwardedHeaderFilter()
}
