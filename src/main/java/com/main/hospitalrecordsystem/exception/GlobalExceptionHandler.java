package com.main.hospitalrecordsystem.exception;

import com.main.hospitalrecordsystem.dto.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ResponseStructure<String>> handleAlreadyExist(AlreadyExistException exception) {
        return new ResponseEntity<>(new ResponseStructure<String>()
                .setData("Failed")
                .setMessage(exception.getMessage())
                .setStatus(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DepartmentNameNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleDepartmentNameNotFound(DepartmentNameNotFoundException exception) {
        return new ResponseEntity<>(new ResponseStructure<String>()
                .setData("Failed")
                .setMessage(exception.getMessage())
                .setStatus(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IdProvidedException.class)
    public ResponseEntity<ResponseStructure<String>> handleIdProvided(IdProvidedException exception) {
        return new ResponseEntity<>(new ResponseStructure<String>()
                .setData("Failed")
                .setMessage(exception.getMessage())
                .setStatus(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NoRecordFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleNoRecordFound(NoRecordFoundException exception) {
        return new ResponseEntity<>(new ResponseStructure<String>()
                .setData("Not Found")
                .setMessage(exception.getMessage())
                .setStatus(HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(IdNotProvidedException.class)
    public ResponseEntity<ResponseStructure<String>> handleIdNotProvided(IdNotProvidedException exception) {
        return new ResponseEntity<>(new ResponseStructure<String>()
                .setData("Failed")
                .setMessage(exception.getMessage())
                .setStatus(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IncompleteObjectException.class)
    public ResponseEntity<ResponseStructure<String>> handleIncompleteObject(IncompleteObjectException exception) {
        return new ResponseEntity<>(new ResponseStructure<String>()
                .setData("Failed")
                .setMessage(exception.getMessage())
                .setStatus(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

}

