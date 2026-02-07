package com.main.hospitalrecordsystem.exception;

public class DepartmentNameNotFoundException extends RuntimeException{
    public DepartmentNameNotFoundException(String message) {
        super(message);
    }
}
