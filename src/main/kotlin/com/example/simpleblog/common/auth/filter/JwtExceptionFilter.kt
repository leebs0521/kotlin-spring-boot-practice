package com.example.simpleblog.common.auth.filter

import com.example.simpleblog.common.exception.ErrorCode
import com.example.simpleblog.common.response.CmResDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
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
      filterChain.doFilter(request, response);
    } catch (e: JwtException) {
      if (ErrorCode.UNKNOWN_ERROR.message == e.message) {
        setResponse(response, ErrorCode.UNKNOWN_ERROR);
      }
      //잘못된 타입의 토큰인 경우
      else if (ErrorCode.WRONG_TYPE_TOKEN.message == e.message) {
        setResponse(response, ErrorCode.WRONG_TYPE_TOKEN);
      }
      //토큰 만료된 경우
      else if (ErrorCode.EXPIRED_TOKEN.message == e.message) {
        setResponse(response, ErrorCode.EXPIRED_TOKEN);
      }
      //지원되지 않는 토큰인 경우
      else if (ErrorCode.UNSUPPORTED_TOKEN.message == e.message) {
        setResponse(response, ErrorCode.UNSUPPORTED_TOKEN);
      } else {
        setResponse(response, ErrorCode.ACCESS_DENIED);
      }
    }
  }

  @Throws(RuntimeException::class, IOException::class)
  private fun setResponse(response: HttpServletResponse, errorCode: ErrorCode) {
    response.status = HttpServletResponse.SC_UNAUTHORIZED
    val cmResDto = CmResDto(HttpStatus.UNAUTHORIZED, errorCode.message, null)
    response.characterEncoding = "UTF-8"
    response.contentType = "application/json;charset=UTF-8"
    response.writer.print(objectMapper.writeValueAsString(cmResDto))
  }

}