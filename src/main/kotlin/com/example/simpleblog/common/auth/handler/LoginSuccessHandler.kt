package com.example.simpleblog.common.auth.handler

import com.example.simpleblog.common.auth.jwt.JwtProvider
import com.example.simpleblog.common.auth.details.PrincipalDetails
import com.example.simpleblog.common.response.ApiResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler

class LoginSuccessHandler(
    private val objectMapper: ObjectMapper,
    private val jwtProvider: JwtProvider
) : SimpleUrlAuthenticationSuccessHandler() {

  private val log = LoggerFactory.getLogger(this::class.java)

  override fun onAuthenticationSuccess(
      request: HttpServletRequest,
      response: HttpServletResponse,
      authentication: Authentication
  ) {
    log.info("로그인 성공")
    val principal = authentication.principal as PrincipalDetails
    val accessToken = jwtProvider.generateAccessToken(principal.username)
    val refreshToken = jwtProvider.generateRefreshToken(principal.username)

    jwtProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken)

    setResponse(response, HttpStatus.OK, "login success")

    log.info("AccessToken & RefreshToken 발급: ${principal.username}")
  }

  private fun setResponse(response: HttpServletResponse, status: HttpStatus, message: String) {
    response.status = status.value()
    response.characterEncoding = "UTF-8"
    response.contentType = "application/json;charset=UTF-8"

    val apiResponse = ApiResponse.of(status, message, null)
    response.writer.write(objectMapper.writeValueAsString(apiResponse))
  }
}
