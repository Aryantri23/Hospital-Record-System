package com.main.hospitalrecordsystem.exception;

public class NoRecordFoundException extends RuntimeException {
    public NoRecordFoundException(String message) {
        super(message);
    }
}
