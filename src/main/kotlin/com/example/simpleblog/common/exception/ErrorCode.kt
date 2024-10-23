package com.example.simpleblog.common.exception

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(
    val code: HttpStatus,
    val message: String
) {

  // Member Error
  NOT_FOUND_MEMBER(NOT_FOUND, "존재하지 않는 멤버입니다."),
  DUPLICATED_MEMBER_EMAIL(CONFLICT, "이미 존재하는 이메일입니다."),
  INVALID_PASSWORD(BAD_REQUEST, "일치하지 않는 패스워드입니다."),

  // Post Error
  NOT_FOUND_POST(NOT_FOUND, "존재하지 않는 게시글입니다."),

  // Jwt Error
  UNKNOWN_ERROR(UNAUTHORIZED, "인증 토큰이 존재하지 않습니다."),
  WRONG_TYPE_TOKEN(UNAUTHORIZED, "잘못된 토큰 정보입니다."),
  EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰 정보입니다."),
  UNSUPPORTED_TOKEN(UNAUTHORIZED, "지원하지 않는 토큰 방식입니다."),
  ACCESS_DENIED(UNAUTHORIZED, "알 수 없는 이유로 요청이 거절되었습니다.");

}