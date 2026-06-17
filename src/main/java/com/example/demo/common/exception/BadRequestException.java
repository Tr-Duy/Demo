package com.example.demo.common.exception;
//xu ly loi yeu cau khong hop le
public class BadRequestException extends  RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
