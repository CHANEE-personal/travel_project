package com.travel.exception;

public interface BaseExceptionType {
    // errorCode
    String getErrorCode();

    // HttpStatus
    int getHttpStatus();

    // errorMessage
    String getErrorMessage();
}
