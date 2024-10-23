package com.example.simpleblog.common.auth.filter

import com.example.simpleblog.common.auth.JwtManger
import com.example.simpleblog.common.auth.PrincipalDetails
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
    private val jwtManger: JwtManger
) : OncePerRequestFilter() {
  private val log = LoggerFactory.getLogger(this::class.java)

  override fun doFilterInternal(
      request: HttpServletRequest,
      response: HttpServletResponse,
      filterChain: FilterChain) {

    log.info("인증 요청 시도")

    val header = request.getHeader(jwtManger.accessTokenHeader)
    if (header == null) {
      filterChain.doFilter(request, response)
      return
    }

    val accessToken = header.replace(jwtManger.bearerPrefix, "")

    if (jwtManger.validateToken(accessToken)) {

      val memberEmail = jwtManger.getMemberEmail(accessToken)
      val member = memberRepository.findByEmail(memberEmail)
      val principalDetails = PrincipalDetails(member)

      val authentication: Authentication = UsernamePasswordAuthenticationToken(
          principalDetails,
          principalDetails.password,
          principalDetails.authorities
      )

      SecurityContextHolder.getContext().authentication = authentication

      filterChain.doFilter(request, response)
    }
  }
}