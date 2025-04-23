package service.config.signing

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("application.signing")
class SigningProperties(
    val password: CharArray
)
