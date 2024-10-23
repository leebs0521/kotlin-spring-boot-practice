package com.example.simpleblog.common.response

import com.example.simpleblog.common.exception.BusinessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiControllerAdvice {
  @ExceptionHandler(BindException::class)
  fun bindException(e: BindException): ResponseEntity<ApiResponse<*>> {
    return ResponseEntity.badRequest()
        .body(
            ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.bindingResult.allErrors[0].defaultMessage.toString(),
            )
        )
  }

  @ExceptionHandler(BusinessException::class)
  fun businessException(e: BusinessException): ResponseEntity<*> {
    return ResponseEntity.status(e.errorCode.code)
        .body(
            ApiResponse.of(
                e.errorCode.code,
                e.errorCode.message
            )
        )
  }

}