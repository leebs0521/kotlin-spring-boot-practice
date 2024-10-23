package com.example.simpleblog.common.exception

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(
    val code: String,
    val message: String
) {

  INVALID_INPUT_VALUE("C001", "Invalid input value."),
  ENTITY_NOT_FOUND("C002", "Entity not found."),

  UNKNOWN_ERROR("C003","인증 토큰이 존재하지 않습니다."),
  WRONG_TYPE_TOKEN("C004","잘못된 토큰 정보입니다."),
  EXPIRED_TOKEN("C005","만료된 토큰 정보입니다."),
  UNSUPPORTED_TOKEN("C006","지원하지 않는 토큰 방식입니다."),
  ACCESS_DENIED("C007","알 수 없는 이유로 요청이 거절되었습니다.");

}