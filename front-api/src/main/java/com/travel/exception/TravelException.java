package com.travel.exception;

import lombok.Getter;

public class TravelException extends RuntimeException {
    @Getter
    private final BaseExceptionType baseExceptionType;

    public TravelException(BaseExceptionType baseExceptionType) {
        super(baseExceptionType.getErrorMessage());
        this.baseExceptionType = baseExceptionType;
    }
}
