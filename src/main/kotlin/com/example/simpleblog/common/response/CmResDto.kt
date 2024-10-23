package com.example.simpleblog.common.response

data class CmResDto<T>(
    val resultCode: T,
    val resultMsg: String,
    val data: T
)
