package com.example.simpleblog.service

import com.example.simpleblog.common.exception.BusinessException
import com.example.simpleblog.common.exception.ErrorCode.NOT_FOUND_MEMBER
import com.example.simpleblog.domain.member.MemberRepository
import com.example.simpleblog.domain.member.MemberResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {

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

}