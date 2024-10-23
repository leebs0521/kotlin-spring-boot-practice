package com.example.simpleblog.common.exception

class BusinessException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message) {


}