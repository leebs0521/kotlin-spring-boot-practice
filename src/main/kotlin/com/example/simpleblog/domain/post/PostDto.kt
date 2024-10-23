package com.example.simpleblog.domain.post

import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.domain.member.MemberResponseDto
import jakarta.validation.constraints.NotNull

data class PostSaveRequestDto(

    @field:NotNull(message = "require title")
    val title: String?,
    val content: String,
    @field:NotNull(message = "require memberId")
    val memberId: Long?
) {
  fun toEntity(member: Member): Post {
    return Post(
        title = this.title ?: "",
        content = this.content,
        member = member
    )
  }
}

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val member: MemberResponseDto
) {
  companion object {
    fun from(post: Post): PostResponseDto {
      return PostResponseDto(
          id = post.id!!,
          title = post.title,
          content = post.content,
          member = MemberResponseDto.from(post.member)
      )
    }
  }
}