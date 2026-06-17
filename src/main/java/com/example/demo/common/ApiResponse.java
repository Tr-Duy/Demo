package com.example.demo.common;

import java.time.LocalDateTime;
//<T data> ham tra ve du lieu ,<Void> ham tra ve nhung khong kem dlieu
//file định nghĩa fomat reponse trả ve client - mọi API trong project deu tra ve cau truc
public record ApiResponse<T>(boolean success, T data, String message, LocalDateTime timestamp) {
    public static <T> ApiResponse<T> success(T data, String message){
        return new ApiResponse<>(true,data,message,LocalDateTime.now());
    }
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(true,data,"Success",LocalDateTime.now());
    }
    public static ApiResponse<Void> successMessage(String message){
        return new ApiResponse<>(true,null,message,LocalDateTime.now());
    }
    public static ApiResponse<Void> error(String message){
        return new ApiResponse<>(false,null,message,LocalDateTime.now());
    }
    public static <T> ApiResponse<T> error(String message, T data){
        return new ApiResponse<>(false,data,message,LocalDateTime.now());
    }
}
