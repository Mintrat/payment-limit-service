package com.ave.limit_service.controllers.handlers;

import com.ave.limit_service.dto.ErrorDto;
import com.ave.limit_service.exception.LimitExceededException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LimitExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorDto notFoundEntity(EntityNotFoundException exceeded) {
        return new ErrorDto("Not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LimitExceededException.class)
    public ErrorDto limitExceeded(LimitExceededException exceeded) {
        return new ErrorDto("Limit exceeded");
    }
}
