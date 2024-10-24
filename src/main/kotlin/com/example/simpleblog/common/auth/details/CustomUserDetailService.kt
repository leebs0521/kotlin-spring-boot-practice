package com.example.simpleblog.common.auth.details

import com.example.simpleblog.common.exception.BusinessException
import com.example.simpleblog.common.exception.ErrorCode.NOT_FOUND_MEMBER
import com.example.simpleblog.domain.member.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    private val memberRepository: MemberRepository,
) : UserDetailsService {

  override fun loadUserByUsername(email: String): UserDetails {
    val member = memberRepository.findByEmail(email) ?: throw BusinessException(NOT_FOUND_MEMBER)
    return CustomUserDetails(member)
  }


}