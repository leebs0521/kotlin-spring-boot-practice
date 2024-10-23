package com.example.simpleblog.domain.member

import com.example.simpleblog.domain.AuditingEntity
import jakarta.persistence.*
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
@Table(name = "Member")
class Member(
    email: String,
    password: String,
    role: Role
) : AuditingEntity() {

  @Column(name = "email", nullable = false)
  var email: String = email
    protected set

  @Column(name = "password")
  var password: String = password
    protected set

  @Enumerated(EnumType.STRING)
  var role: Role = role
    protected set

  fun passwordEncode(passwordEncoder: PasswordEncoder): Unit {
    this.password = passwordEncoder.encode(this.password)
  }

  companion object {
    fun createFakerMember(memberId: Long): Member {
      val member = Member("", "", Role.USER)
      member.id = memberId
      return member
    }
  }
}

enum class Role {
  USER, ADMIN
}