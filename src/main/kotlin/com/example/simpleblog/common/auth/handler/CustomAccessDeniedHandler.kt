package com.example.simpleblog.common.auth.handler

import com.example.simpleblog.common.response.ApiResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler

class CustomAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {
  private val log = LoggerFactory.getLogger(this::class.java)

  override fun handle(
      request: HttpServletRequest,
      response: HttpServletResponse,
      accessDeniedException: AccessDeniedException
  ) {
    log.warn("인증 성공, 인가 실패: ${accessDeniedException.message}")
    response.status = HttpServletResponse.SC_FORBIDDEN
    val resDto = ApiResponse.of(HttpStatus.FORBIDDEN, "인가 실패", null)
    response.characterEncoding = "UTF-8"
    response.contentType = "application/json;charset=UTF-8"
    response.writer.write(objectMapper.writeValueAsString(resDto));
  }
}