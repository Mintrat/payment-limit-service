package com.ave.limit_service.controllers.handlers;

import com.ave.limit_service.dto.ErrorDto;
import com.ave.limit_service.exception.LimitErrorStatusException;
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
    public ErrorDto notFoundEntity(EntityNotFoundException exception) {
        return new ErrorDto(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({LimitExceededException.class, LimitErrorStatusException.class})
    public ErrorDto limitExceeded(RuntimeException exception) {
        return new ErrorDto(exception.getMessage());
    }
}
