package com.example.simpleblog.common.auth.filter

import com.example.simpleblog.domain.member.MemberLoginRequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class LoginFilter(
    private val objectMapper: ObjectMapper,
) : UsernamePasswordAuthenticationFilter() {

  private val log = LoggerFactory.getLogger(this::class.java)

  override fun attemptAuthentication(
      request: HttpServletRequest,
      response: HttpServletResponse
  ): Authentication {

    val loginDto = objectMapper.readValue(request.inputStream, MemberLoginRequestDto::class.java)
    log.info("로그인 시도 유저: ${loginDto.email}")

    val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)

    return this.authenticationManager.authenticate(authenticationToken)
  }
}