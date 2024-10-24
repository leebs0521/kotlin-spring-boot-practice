package com.example.simpleblog.common.auth.details

import com.example.simpleblog.domain.member.Member
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class PrincipalDetails(
    member: Member
) : UserDetails {

  var member: Member = member
    private set

  private val log = LoggerFactory.getLogger(this::class.java)

  override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
    log.info("Role 검증")
    val collection: MutableCollection<GrantedAuthority> = ArrayList()
    collection.add(GrantedAuthority { "ROLE_" + member.role })

    return collection
  }

  override fun getPassword(): String {
    return member.password
  }

  override fun getUsername(): String {
    return member.email
  }
}