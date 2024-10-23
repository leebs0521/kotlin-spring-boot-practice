package com.example.simpleblog.service

import com.example.simpleblog.common.auth.PrincipalDetails
import com.example.simpleblog.domain.member.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthService(
  private val memberRepository: MemberRepository
): UserDetailsService {

  val log = LoggerFactory.getLogger(this::class.java)

  override fun loadUserByUsername(email: String): UserDetails {

    val member = memberRepository.findByEmail(email)

    if(member != null) return PrincipalDetails(member) else throw RuntimeException("member not found!")
  }
}