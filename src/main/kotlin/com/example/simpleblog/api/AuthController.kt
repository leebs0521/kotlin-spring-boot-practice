package com.example.simpleblog.api

import com.example.simpleblog.common.response.ApiResponse
import com.example.simpleblog.domain.member.MemberSaveRequestDto
import com.example.simpleblog.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/auth")
@RestController
class AuthController(
    private val memberService: MemberService
) {

  @GetMapping("/login")
  fun login(@Valid @RequestBody memberSaveReq: MemberSaveRequestDto) {
    // login
  }

  @PostMapping("/signup")
  fun register(@RequestBody @Valid dto: MemberSaveRequestDto): ResponseEntity<*> {
    val member = memberService.saveMember(dto)
    return ResponseEntity.status(HttpStatus.CREATED).body(
        ApiResponse.of(HttpStatus.CREATED, "save member", member)
    )
  }
}