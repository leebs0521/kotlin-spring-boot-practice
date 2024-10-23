package com.example.simpleblog.service

import com.example.simpleblog.common.exception.BusinessException
import com.example.simpleblog.common.exception.ErrorCode.NOT_FOUND_POST
import com.example.simpleblog.domain.post.PostRepository
import com.example.simpleblog.domain.post.PostResponseDto
import com.example.simpleblog.domain.post.PostSaveRequestDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
    private val memberService: MemberService
) {

  @Transactional(readOnly = true)
  fun findPosts(pageable: Pageable): Page<PostResponseDto> {
    return postRepository.findPosts(pageable).map { PostResponseDto.from(it) }
  }

  @Transactional
  fun savePost(dto: PostSaveRequestDto): PostResponseDto {
    val member = memberService.getMemberByIdOrThrow(dto.memberId!!)
    val post = dto.toEntity(member)
    postRepository.save(post)

    return PostResponseDto.from(post)
  }

  @Transactional
  fun deletePost(id: Long) {
    val post = getPostByIdOrThrow(id)
    return postRepository.deleteById(id)
  }

  @Transactional(readOnly = true)
  fun findPostById(id: Long): PostResponseDto {
    val post = getPostByIdOrThrow(id)
    return PostResponseDto.from(post)
  }

  private fun getPostByIdOrThrow(id: Long) =
      postRepository.findByIdOrNull(id) ?: throw BusinessException(NOT_FOUND_POST)
}