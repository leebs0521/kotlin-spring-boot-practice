package com.example.simpleblog.common.auth.handler

import com.example.simpleblog.common.auth.JwtManger
import com.example.simpleblog.common.auth.PrincipalDetails
import com.example.simpleblog.common.response.CmResDto
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler

class LoginSuccessHandler(
    private val objectMapper: ObjectMapper,
    private val jwtManger: JwtManger
) : SimpleUrlAuthenticationSuccessHandler() {

  private val log = LoggerFactory.getLogger(this::class.java)

  override fun onAuthenticationSuccess(
      request: HttpServletRequest,
      response: HttpServletResponse,
      authentication: Authentication
  ) {
    log.info("로그인 성공")
    val principal: PrincipalDetails = authentication.principal as PrincipalDetails
    val generateAccessToken = jwtManger.generateAccessToken(principal)
    response.setHeader(jwtManger.accessTokenHeader, jwtManger.bearerPrefix + generateAccessToken)

    response.status = HttpServletResponse.SC_OK
    val resDto = CmResDto(HttpStatus.OK, "login success", null)
    response.characterEncoding = "UTF-8"
    response.contentType = "application/json;charset=UTF-8"
    response.writer.write(objectMapper.writeValueAsString(resDto));

    log.info("AccessToken 발급: ${principal.username}")
  }
}