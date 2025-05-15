package com.playus.communityservice.global.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityName) {
        super(entityName + "을(를) 찾을 수 없습니다.");
    }
}
