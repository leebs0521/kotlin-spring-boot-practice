package com.example.simpleblog.api

import com.example.simpleblog.common.response.ApiResponse
import com.example.simpleblog.service.MemberService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class MemberController(
    private val memberService: MemberService
) {

  @GetMapping("/members")
  fun findAll(@PageableDefault(size = 10) pageable: Pageable): ResponseEntity<*> {

    val members = memberService.findAll(pageable)
    return ResponseEntity.ok(ApiResponse.ok("find all members", members))
  }

  @GetMapping("/member/{id}")
  fun findById(@PathVariable id: Long): ResponseEntity<*> {
    val member = memberService.findMemberById(id)

    return ResponseEntity.ok(ApiResponse.ok("find Member by id: $id", member))
  }

  @DeleteMapping("/member/{id}")
  fun deleteById(@PathVariable id: Long): ResponseEntity<*> {

    memberService.deleteMember(id)

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
        ApiResponse.of(HttpStatus.NO_CONTENT, "delete member by id: $id", null)
    )
  }

}