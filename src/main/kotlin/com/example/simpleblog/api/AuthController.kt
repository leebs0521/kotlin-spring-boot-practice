package com.example.simpleblog.api

import com.example.simpleblog.common.response.ApiResponse
import com.example.simpleblog.domain.member.MemberSaveRequestDto
import com.example.simpleblog.service.AuthService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RequestMapping("/api/auth")
@RestController
class AuthController(
    private val authService: AuthService
) {

  @GetMapping("/login")
  fun login(@Valid @RequestBody memberSaveReq: MemberSaveRequestDto) {
    // login
  }

  @PostMapping("/logout")
  fun logout() {
    // logout
  }

  @PostMapping("/signup")
  fun register(@RequestBody @Valid dto: MemberSaveRequestDto): ResponseEntity<*> {
    val member = authService.saveMember(dto)
    return ResponseEntity.status(HttpStatus.CREATED).body(
        ApiResponse.of(HttpStatus.CREATED, "save member", member)
    )
  }

  @GetMapping("/reissue")
  fun reissueAccessToken(@CookieValue(value = "Authorization-refresh", defaultValue = "") refreshToken: String,
                         response: HttpServletResponse): ResponseEntity<*> {
    authService.reIssueAccessToken(response, refreshToken)
    return ResponseEntity.ok().body(ApiResponse.ok("AccessToken 재발급 성공", null))
  }
}