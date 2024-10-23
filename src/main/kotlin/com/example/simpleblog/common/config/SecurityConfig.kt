package com.example.simpleblog.common.config

import com.example.simpleblog.common.auth.JwtManger
import com.example.simpleblog.common.auth.filter.JwtAuthenticationFilter
import com.example.simpleblog.common.auth.filter.JwtExceptionFilter
import com.example.simpleblog.common.auth.filter.LoginFilter
import com.example.simpleblog.common.auth.handler.CustomAccessDeniedHandler
import com.example.simpleblog.common.auth.handler.LoginFailureHandler
import com.example.simpleblog.common.auth.handler.LoginSuccessHandler
import com.example.simpleblog.domain.member.MemberRepository
import com.example.simpleblog.service.AuthService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.web.cors.CorsConfiguration


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authService: AuthService,
    private val memberRepository: MemberRepository,
    private val objectMapper: ObjectMapper,
    private val jwtManger: JwtManger,
) {

  @Bean
  @Throws(Exception::class)
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http
        .csrf(CsrfConfigurer<HttpSecurity>::disable)
        .formLogin(FormLoginConfigurer<HttpSecurity>::disable)
        .httpBasic(HttpBasicConfigurer<HttpSecurity>::disable)
        .headers { it.frameOptions(HeadersConfigurer<HttpSecurity>.FrameOptionsConfig::sameOrigin) }
        .sessionManagement {
          it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .authorizeHttpRequests { authorize ->
          authorize
              .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
              .requestMatchers("/static/**", "/templates/**").permitAll()
              .requestMatchers("/admin-test").hasRole("ADMIN")
              .requestMatchers("/api/v1/**").authenticated()
              .anyRequest().authenticated()
        }
        .cors {
          this.corsConfig()
        }
        .exceptionHandling {
          it.accessDeniedHandler(customAccessDeniedHandler())
        }
        .addFilterAfter(loginFilter(), LogoutFilter::class.java)
        .addFilterBefore(jwtAuthenticationFilter(), LoginFilter::class.java)
        .addFilterBefore(jwtExceptionFilter(), JwtAuthenticationFilter::class.java)

    return http.build()
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }

  @Bean
  fun authenticationManger(): AuthenticationManager {
    val provider = DaoAuthenticationProvider()
    provider.setPasswordEncoder(passwordEncoder())
    provider.setUserDetailsService(authService)
    return ProviderManager(provider)
  }

  @Bean
  fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
    return JwtAuthenticationFilter(memberRepository, jwtManger)
  }

  @Bean
  fun loginFilter(): LoginFilter {
    val loginFilter = LoginFilter(objectMapper)
    loginFilter.setAuthenticationManager(authenticationManger())
    loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler())
    loginFilter.setAuthenticationFailureHandler(loginFailureHandler())
    loginFilter.setFilterProcessesUrl("/login")

    return loginFilter
  }

  @Bean
  fun loginSuccessHandler(): LoginSuccessHandler {
    return LoginSuccessHandler(objectMapper, jwtManger)
  }

  @Bean
  fun loginFailureHandler(): LoginFailureHandler {
    return LoginFailureHandler(objectMapper)
  }

  @Bean
  fun jwtExceptionFilter(): JwtExceptionFilter {
    return JwtExceptionFilter(objectMapper)
  }

  @Bean
  fun customAccessDeniedHandler(): CustomAccessDeniedHandler {
    return CustomAccessDeniedHandler(objectMapper)
  }

  @Bean
  fun corsConfig(): CorsConfiguration {
    return CorsConfiguration().apply {
      this.allowedOrigins = listOf("*")
      this.allowedMethods = listOf("*")
      this.allowedHeaders = listOf("*")
      this.allowCredentials = true
    }
  }


}