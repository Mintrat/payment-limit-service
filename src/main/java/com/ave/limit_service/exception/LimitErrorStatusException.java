package com.ave.limit_service.exception;

public class LimitErrorStatusException extends RuntimeException{
    public LimitErrorStatusException(String message) {
        super(message);
    }
}
