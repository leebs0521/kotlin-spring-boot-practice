package com.example.simpleblog.common.response

import org.springframework.http.HttpStatus

data class ApiResponse<T>(
    val status: HttpStatus,
    val message: String,
    val data: T
) {
  val code: Int = status.value()

  companion object {
    fun <T> of(status: HttpStatus, message: String, data: T): ApiResponse<T> {
      return ApiResponse(status, message, data)
    }

    fun <T> of(status: HttpStatus, data: T): ApiResponse<T> {
      return of(status, status.name, data)
    }

    fun <T> ok(message: String, data: T): ApiResponse<T> {
      return of(HttpStatus.OK, message, data)
    }

    fun <T> ok(data: T): ApiResponse<T> {
      return of(HttpStatus.OK, HttpStatus.OK.name, data)
    }
  }
}

