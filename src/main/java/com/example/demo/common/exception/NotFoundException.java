package com.example.demo.common.exception;
//ham xu ly loi khi khong tim thay
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
