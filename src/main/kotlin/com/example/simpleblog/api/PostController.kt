package com.example.simpleblog.api

import com.example.simpleblog.common.response.ApiResponse
import com.example.simpleblog.domain.post.PostSaveRequestDto
import com.example.simpleblog.service.PostService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class PostController(
    private val postService: PostService
) {

  @GetMapping("/posts")
  fun findPosts(@PageableDefault(size = 10) pageable: Pageable): ResponseEntity<*> {
    val posts = postService.findPosts(pageable)

    return ResponseEntity.ok(ApiResponse.ok("find posts", posts))
  }

  @GetMapping("/post/{id}")
  fun findById(@PathVariable id: Long): ResponseEntity<*> {
    val post = postService.findPostById(id)

    return ResponseEntity.ok(ApiResponse.ok("find post by id: $id", post))
  }

  @DeleteMapping("/post/{id}")
  fun deleteById(@PathVariable id: Long): ResponseEntity<*> {
    postService.deletePost(id)

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
        ApiResponse.of(HttpStatus.NO_CONTENT, "delete Post by id: $id"))
  }

  @PostMapping("/post")
  fun save(@Valid @RequestBody dto: PostSaveRequestDto): ResponseEntity<*> {
    val post = postService.savePost(dto)

    return ResponseEntity.status(HttpStatus.CREATED).body(
        ApiResponse.of(HttpStatus.CREATED, "save post", post))
  }
}