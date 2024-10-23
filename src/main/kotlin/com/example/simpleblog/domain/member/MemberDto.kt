package com.example.simpleblog.domain.member

import jakarta.validation.constraints.NotNull


data class MemberSaveReq(
    @field:NotNull(message = "require email")
    val email: String?,
    val password: String,
    val role: Role
)

data class LoginDto(
    val email: String,
    val password: String
)

fun MemberSaveReq.toEntity(): Member {
  return Member(
      email = this.email?: "",
      password = this.password,
      role = this.role
  )
}

data class MemberRes(
    val id: Long,
    val email: String,
    val password: String,
    val role: Role
)