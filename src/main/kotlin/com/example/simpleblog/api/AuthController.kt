package com.example.simpleblog.api

import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auth")
@RestController
class AuthController(

) {
  val log = LoggerFactory.getLogger(this::class.java)

  @GetMapping("/login")
  fun login(session: HttpSession) {
    session.setAttribute("principal", "pass")
  }

}