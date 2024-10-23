package com.example.simpleblog.domain.member

import jakarta.validation.constraints.NotNull


data class MemberSaveRequestDto(
    @field:NotNull(message = "require email")
    val email: String?,
    val password: String,
    val role: Role
) {
  fun toEntity(): Member {
    return Member(
        email = this.email ?: "",
        password = this.password,
        role = this.role
    )
  }
}

data class MemberLoginRequestDto(
    val email: String,
    val password: String
)


data class MemberResponseDto(
    val id: Long,
    val email: String,
    val password: String,
    val role: Role
) {
  companion object {
    fun from(member: Member): MemberResponseDto {
      return MemberResponseDto(
          id = member.id!!,
          email = member.email,
          password = member.password,
          role = member.role
      )
    }
  }
}