package com.example.simpleblog.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(

) {

    @GetMapping("/health")
    fun healthTest(): String {
        return "hello kotlin-blog"
    }

    @GetMapping("/admin-test")
    fun adminTest(): String {
        return "this is admin page."
    }

}