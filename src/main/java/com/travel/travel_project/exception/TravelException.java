package com.travel.travel_project.exception;

import lombok.Getter;

public class TravelException extends RuntimeException {
    @Getter
    private final BaseExceptionType baseExceptionType;

    public TravelException(BaseExceptionType baseExceptionType, Throwable cause) {
        super(baseExceptionType.getErrorMessage(), cause);
        this.baseExceptionType = baseExceptionType;
    }
}
