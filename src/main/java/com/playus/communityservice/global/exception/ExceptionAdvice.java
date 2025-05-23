package com.playus.communityservice.global.exception;

import com.playus.communityservice.domain.comment.controller.CommentController;
import com.playus.communityservice.domain.post.controller.PostController;
import com.playus.communityservice.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RestControllerAdvice(assignableTypes = {
        PostController.class,
        CommentController.class
})
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse bindExceptionHandler(BindException e) {
        String errorMessage = e.getAllErrors().get(0).getDefaultMessage();
        log.warn("Validation Error: {}", errorMessage);
        return ErrorResponse.badRequestError(errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        String errorMessage = e.getMessage();
        log.warn("IllegalArgumentException: {}", errorMessage);
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
            ForbiddenAccessException.class
    })
    public ErrorResponse handleForbiddenAccessException(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Forbidden Access Error: {}", errorMessage);
        return ErrorResponse.forbiddenError(errorMessage);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName(); // 예: "postId"
        String invalidValue = String.valueOf(ex.getValue()); // 예: "asdf"
        String message = String.format("'%s' 파라미터에 잘못된 값 '%s'이(가) 들어왔습니다.", paramName, invalidValue);
        log.warn("Type Mismatch Error: {}", message);
        return ErrorResponse.badRequestError(message);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse otherExceptionHandler(Exception e) {
        String errorMessage = e.getMessage();
        log.error("Unexpected Error: {}", errorMessage);
        return ErrorResponse.internalServerError("서버 에러가 발생했습니다! 관리자에게 문의해 주세요!");
    }
}
