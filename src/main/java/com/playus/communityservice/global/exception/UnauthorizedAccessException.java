package com.playus.communityservice.global.exception;

public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(String entityName) {
        super("해당 " + entityName + "에 대한 권한이 없습니다.");
    }
}
