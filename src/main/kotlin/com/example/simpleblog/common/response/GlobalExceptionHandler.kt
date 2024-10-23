package com.example.simpleblog.common.response

import com.example.simpleblog.common.exception.EntityNotFoundException
import com.example.simpleblog.common.exception.ErrorCode
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

  val log = LoggerFactory.getLogger(this::class.java)

  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleMethodArgumentNotValidException(e:MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {

    log.error("handleMethodArgumentNotValidException $e")
    val of = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND, e.bindingResult)

    return ResponseEntity(of, HttpStatus.BAD_REQUEST)

  }

  @ExceptionHandler(EntityNotFoundException::class)
  fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<ErrorResponse> {

    log.error("handleEntityNotFoundException $e")
    val of = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND)

    return ResponseEntity(of, HttpStatus.NOT_FOUND)

  }
}