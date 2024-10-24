package com.example.simpleblog.service

import com.example.simpleblog.common.auth.jwt.JwtProvider
import com.example.simpleblog.common.exception.BusinessException
import com.example.simpleblog.common.exception.ErrorCode
import com.example.simpleblog.common.exception.ErrorCode.NOT_FOUND_MEMBER
import com.example.simpleblog.domain.member.MemberRepository
import com.example.simpleblog.domain.member.MemberResponseDto
import com.example.simpleblog.domain.member.MemberSaveRequestDto
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider
) {
  private val log = LoggerFactory.getLogger(this::class.java)

  @Transactional(readOnly = true)
  fun findAll(pageable: Pageable): Page<MemberResponseDto> = memberRepository.findMembers(pageable).map { MemberResponseDto.from(it) }

  @Transactional
  fun deleteMember(id: Long) {
    val member = getMemberByIdOrThrow(id)
    memberRepository.delete(member)
  }

  @Transactional(readOnly = true)
  fun findMemberById(id: Long): MemberResponseDto {
    val member = getMemberByIdOrThrow(id)
    return MemberResponseDto.from(member)
  }

  fun getMemberByIdOrThrow(id: Long) =
      memberRepository.findByIdOrNull(id) ?: throw BusinessException(NOT_FOUND_MEMBER)

  @Transactional
  fun saveMember(dto: MemberSaveRequestDto): MemberResponseDto {

    validateEmail(dto.email!!)

    val member = dto.toEntity()
    member.passwordEncode(passwordEncoder)

    memberRepository.save(member)

    return MemberResponseDto.from(member)
  }

  fun reIssueAccessToken(response: HttpServletResponse, refreshToken: String) {
    log.info("AccessToken 재발행 요청")
    jwtProvider.validateToken(refreshToken)
    val username = jwtProvider.getUsername(refreshToken)
    val member = getMemberByEmailOrThrow(username)
    val accessToken = jwtProvider.generateAccessToken(member.email)
    jwtProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken)
    log.info("AccessToken 재발행 성공")
  }

  private fun getMemberByEmailOrThrow(email: String) =
      memberRepository.findByEmail(email) ?: throw BusinessException(NOT_FOUND_MEMBER)

  private fun validateEmail(email: String): Unit {
    if (memberRepository.existsByEmail(email))
      throw BusinessException(ErrorCode.DUPLICATED_MEMBER_EMAIL)
  }
}