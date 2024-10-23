package com.example.simpleblog.common.auth.filter

import com.example.simpleblog.common.exception.ErrorCode
import com.example.simpleblog.common.response.ApiResponse
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class JwtExceptionFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

  override fun doFilterInternal(
      request: HttpServletRequest,
      response: HttpServletResponse,
      filterChain: FilterChain
  ) {
    try {
      filterChain.doFilter(request, response)
    } catch (e: JwtException) {
      handleJwtException(response, e)
    }
  }

  private fun handleJwtException(response: HttpServletResponse, exception: JwtException) {
    val errorCode = when (exception.message) {
      ErrorCode.UNKNOWN_ERROR.message -> ErrorCode.UNKNOWN_ERROR
      ErrorCode.WRONG_TYPE_TOKEN.message -> ErrorCode.WRONG_TYPE_TOKEN
      ErrorCode.EXPIRED_TOKEN.message -> ErrorCode.EXPIRED_TOKEN
      ErrorCode.UNSUPPORTED_TOKEN.message -> ErrorCode.UNSUPPORTED_TOKEN
      else -> ErrorCode.ACCESS_DENIED
    }
    setResponse(response, errorCode)
  }

  @Throws(RuntimeException::class, IOException::class)
  private fun setResponse(response: HttpServletResponse, errorCode: ErrorCode) {
    response.status = SC_UNAUTHORIZED
    val cmResDto = ApiResponse.of(UNAUTHORIZED, errorCode.message, null)
    response.characterEncoding = "UTF-8"
    response.contentType = "application/json;charset=UTF-8"
    response.writer.print(objectMapper.writeValueAsString(cmResDto))
  }

}