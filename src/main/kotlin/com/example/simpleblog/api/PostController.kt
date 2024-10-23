package com.example.simpleblog.api

import com.example.simpleblog.domain.post.PostSaveReq
import com.example.simpleblog.service.PostService
import com.example.simpleblog.common.response.CmResDto
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class PostController (
    private val postService: PostService
){

  @GetMapping("/posts")
  fun findPosts(@PageableDefault(size = 10) pageable: Pageable): CmResDto<*> {
    return CmResDto(HttpStatus.OK, "find posts", postService.findPosts(pageable))
  }

  @GetMapping("/post/{id}")
  fun findById(@PathVariable id: Long): CmResDto<Any> {
    return CmResDto(HttpStatus.OK, "find Post by id", postService.findPostById(id))

  }

  @DeleteMapping("/post/{id}")
  fun deleteById(@PathVariable id: Long): CmResDto<Any> {
    return CmResDto(HttpStatus.OK, "delete Post by id", postService.deletePost(id))
  }

  @PostMapping("/post")
  fun save(@Valid @RequestBody dto: PostSaveReq): CmResDto<*> {
    return CmResDto(HttpStatus.OK, "save Post", postService.savePost(dto))
  }
}