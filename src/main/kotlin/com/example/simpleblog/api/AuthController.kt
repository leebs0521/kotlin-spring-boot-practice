package com.example.simpleblog.api

import com.example.simpleblog.domain.member.MemberSaveReq
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auth")
@RestController
class AuthController(

) {
  val log = LoggerFactory.getLogger(this::class.java)

  @GetMapping("/login")
  fun login(@Valid @RequestBody memberSaveReq: MemberSaveReq) {

  }

}