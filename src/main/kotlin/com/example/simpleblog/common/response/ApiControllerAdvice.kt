package com.example.simpleblog.common.response

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
                status = HttpStatus.BAD_REQUEST,
                message = e.bindingResult.allErrors[0].defaultMessage.toString(),
                data = null
            ))
  }

}