package com.example.simpleblog.common.auth.handler

import com.example.simpleblog.common.response.ApiResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler

class CustomLogoutSuccessHandler(
    private val objectMapper: ObjectMapper
) : LogoutSuccessHandler {

  private val log = LoggerFactory.getLogger(this::class.java)

  override fun onLogoutSuccess(
      request: HttpServletRequest,
      response: HttpServletResponse,
      authentication: Authentication
  ) {
    log.info("로그아웃 성공")

    clearAuthentication()

    setResponse(response)
  }

  private fun clearAuthentication() {
    SecurityContextHolder.getContext().authentication = null
    SecurityContextHolder.clearContext()
  }

  private fun setResponse(response: HttpServletResponse) {
    response.status = HttpServletResponse.SC_OK
    val apiResponse = ApiResponse.of(HttpStatus.OK, "logout success", null)
    response.characterEncoding = "UTF-8"
    response.contentType = "application/json;charset=UTF-8"
    response.writer.write(objectMapper.writeValueAsString(apiResponse))
  }
}
