package com.example.simpleblog.domain.post

import com.example.simpleblog.domain.AuditingEntity
import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.domain.member.toDto
import jakarta.persistence.*

@Entity
@Table(name = "Post")
class Post(
    title: String,
    content: String,
    member: Member
) : AuditingEntity() {

  @Column(name = "title", nullable = false)
  var title: String = title
    protected set

  @Column(name = "content")
  var content: String = content
    protected set

  @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
  var member: Member = member
    protected set
}

fun Post.toDto(): PostRes =
    PostRes(
        id = this.id!!,
        title = this.title,
        content = this.content,
        member = this.member.toDto()
    )
