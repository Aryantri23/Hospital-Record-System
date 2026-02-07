package com.main.hospitalrecordsystem.dto;

public class ResponseStructure<T> {

    private T data;

    private String message;

    private Integer status;

    public T getData() {
        return data;
    }

    public ResponseStructure<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseStructure<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public ResponseStructure<T> setStatus(Integer status) {
        this.status = status;
        return this;
    }
}
