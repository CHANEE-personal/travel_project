package com.travel.travel_project.admin.exception;

public interface BaseExceptionType {
    // errorCode
    String getErrorCode();

    // HttpStatus
    int getHttpStatus();

    // errorMessage
    String getErrorMessage();
}
