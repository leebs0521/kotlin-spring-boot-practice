package com.example.simpleblog.common.auth.handler

import com.example.simpleblog.common.response.ApiResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler

class LoginFailureHandler(
    private val objectMapper: ObjectMapper
) : SimpleUrlAuthenticationFailureHandler() {

  private val log = LoggerFactory.getLogger(this::class.java)

  override fun onAuthenticationFailure(
      request: HttpServletRequest,
      response: HttpServletResponse,
      exception: AuthenticationException
  ) {
    log.info("로그인 실패")

    setResponse(response, HttpStatus.BAD_REQUEST, "login failed")
  }

  private fun setResponse(response: HttpServletResponse, status: HttpStatus, message: String) {
    response.status = status.value()
    response.characterEncoding = "UTF-8"
    response.contentType = "application/json;charset=UTF-8"

    val apiResponse = ApiResponse.of(status, message, null)
    response.writer.write(objectMapper.writeValueAsString(apiResponse))
  }
}
