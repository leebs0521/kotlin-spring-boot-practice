package com.example.simpleblog.service

import com.example.simpleblog.common.exception.MemberNotFoundException
import com.example.simpleblog.domain.member.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {

  @Transactional(readOnly = true)
  fun findAll(pageable: Pageable): Page<MemberRes> = memberRepository.findMembers(pageable).map { it.toDto() }


  @Transactional
  fun saveMember(dto: MemberSaveReq): MemberRes {
    val member = dto.toEntity()
    member.passwordEncode(passwordEncoder)
    memberRepository.save(member)

    return member.toDto()
  }

  @Transactional
  fun deleteMember(id: Long) {
    return memberRepository.deleteById(id)
  }

  @Transactional(readOnly = true)
  fun findMemberById(id: Long): MemberRes {
    return memberRepository.findById(id).orElseThrow {
      throw MemberNotFoundException(id)
    }.toDto()
  }
}