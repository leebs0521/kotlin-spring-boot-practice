package com.example.simpleblog.common.exception

sealed class EntityNotFoundException(message: String) : BusinessException(message, ErrorCode.ENTITY_NOT_FOUND) {
}

class MemberNotFoundException(id: Long) : EntityNotFoundException("$id not found.") {

}