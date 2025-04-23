package service.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import service.config.security.Authorities.ROLE_CURATOR
import service.config.security.Authorities.SCOPE_API

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration {

    private val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            httpBasic {}
            cors { disable() }
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = STATELESS
            }
            authorizeHttpRequests {
                authorize(anyRequest, hasAuthority(SCOPE_API))
            }
        }
        return http.build()
    }

    @Bean
    fun userDetailService(): UserDetailsService =
        InMemoryUserDetailsManager(
            dummyUser("curator", SCOPE_API, ROLE_CURATOR),
            dummyUser("user", SCOPE_API),
        )

    private fun dummyUser(username: String, vararg authorities: String) =
        User.withUsername(username)
            .password(passwordEncoder.encode(username.reversed()))
            .authorities(*authorities)
            .build()
}
