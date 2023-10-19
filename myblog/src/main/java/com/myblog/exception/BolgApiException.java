package com.myblog.exception;

import org.springframework.http.HttpStatus;

public class BolgApiException extends RuntimeException{

    private HttpStatus status;
    private String message;

    public  BolgApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
