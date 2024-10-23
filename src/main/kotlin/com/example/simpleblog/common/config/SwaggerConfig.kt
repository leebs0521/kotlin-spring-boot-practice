package com.example.simpleblog.common.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

  @Bean
  fun openApi(): OpenAPI {
    return OpenAPI()
        .info(
            Info()
                .title("Kotlin Spring Boot Practice API")
                .version("1.0")
                .description("Kotlin Spring Boot 연습")
        )
        .components(
            Components()
                .addSecuritySchemes(
                    "AccessToken",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
        )
        .addSecurityItem(SecurityRequirement().addList("AccessToken"))
  }
}