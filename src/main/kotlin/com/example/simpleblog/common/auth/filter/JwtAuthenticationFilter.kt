package com.example.simpleblog.common.auth.filter

import com.example.simpleblog.common.auth.jwt.JwtProvider
import com.example.simpleblog.common.auth.details.CustomUserDetails
import com.example.simpleblog.common.exception.BusinessException
import com.example.simpleblog.common.exception.ErrorCode.NOT_FOUND_MEMBER
import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.domain.member.MemberRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val memberRepository: MemberRepository,
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {

  private val log = LoggerFactory.getLogger(this::class.java)

  @Throws(BusinessException::class)
  override fun doFilterInternal(
      request: HttpServletRequest,
      response: HttpServletResponse,
      filterChain: FilterChain
  ) {
    log.info("인증 요청 시도")

    val accessToken = extractAccessToken(request) ?: run {
      filterChain.doFilter(request, response)
      return
    }

    if (jwtProvider.validateToken(accessToken)) {
      val memberEmail = jwtProvider.getUsername(accessToken)
      val member = memberRepository.findByEmail(memberEmail)
          ?: throw BusinessException(NOT_FOUND_MEMBER)

      setAuthentication(member)
    }

    filterChain.doFilter(request, response)
  }

  private fun extractAccessToken(request: HttpServletRequest): String? {
    val header = request.getHeader(jwtProvider.accessHeader)
    return header?.replace(jwtProvider.bearerPrefix, "")
  }

  private fun setAuthentication(member: Member) {
    val userDetails = CustomUserDetails(member)

    val authentication: Authentication = UsernamePasswordAuthenticationToken(
        userDetails,
        userDetails.password,
        userDetails.authorities
    )

    SecurityContextHolder.getContext().authentication = authentication
  }
}
