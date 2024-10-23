package com.example.simpleblog.common.exception

class BusinessException(
    private val errorCode: ErrorCode
) : RuntimeException(errorCode.message) {


}