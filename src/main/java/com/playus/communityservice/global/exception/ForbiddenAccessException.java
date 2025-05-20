package com.playus.communityservice.global.exception;

public class ForbiddenAccessException extends RuntimeException{
    public ForbiddenAccessException(String entityName) {
        super("해당 " + entityName + "에 대한 권한이 없습니다.");
    }
}
