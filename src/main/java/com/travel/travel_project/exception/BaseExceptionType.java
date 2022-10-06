package com.travel.travel_project.exception;

public interface BaseExceptionType {
    // errorCode
    String getErrorCode();

    // HttpStatus
    int getHttpStatus();

    // errorMessage
    String getErrorMessage();
}
