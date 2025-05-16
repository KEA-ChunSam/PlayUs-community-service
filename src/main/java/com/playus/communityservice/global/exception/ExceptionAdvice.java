package com.playus.communityservice.global.exception;

import com.playus.communityservice.domain.post.controller.PostController;
import com.playus.communityservice.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RestControllerAdvice(assignableTypes = {
        PostController.class
})
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse bindExceptionHandler(BindException e) {
        String errorMessage = e.getAllErrors().get(0).getDefaultMessage();
        log.warn("Validation Error: {}", errorMessage);
        return ErrorResponse.badRequestError(errorMessage);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ResponseStatusException.class)
    public ErrorResponse responseStatusExHandler(ResponseStatusException e) {
        String errorMessage = e.getMessage();
        log.warn("INVALID_TOKEN Error: {}", errorMessage);
        return ErrorResponse.unauthorizedError(errorMessage);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            EntityNotFoundException.class
    })
    public ErrorResponse handleEntityNotFoundException(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Entity Not Found Error: {}", errorMessage);
        return ErrorResponse.notFoundError(errorMessage);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({
            UnauthorizedAccessException.class
    })
    public ErrorResponse handleUnauthorizedAccessException(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Unauthorized Access Error: {}", errorMessage);
        return ErrorResponse.forbiddenError(errorMessage);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse otherExceptionHandler(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Unexpected Error: {}", errorMessage);
        return ErrorResponse.internalServerError("서버 에러가 발생했습니다! 관리자에게 문의해 주세요!");
    }
}
