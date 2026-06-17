package com.example.demo.common.exception;
//chuc nang quan ly va xu ly loi tap trung cho toan bo project

import com.example.demo.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

//controlleradvice + ResponseBody gom toan bo logic xu ly loi ve file nay
@RestControllerAdvice
public class GlobalException {
    //exceptionhandler dinh danh loi , vd khi gap loi x chay ham nay
    //reponseEntity: he thong tra ve dung loi HTTP
    //handle ... cnang hung loi cu the ,phan hoi json,bao loi HTTP
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
    }//tra ve 404
    //ham xu ly loi BadRequesException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(Exception e){
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }//kq tra ve 400
    //ham xu ly loi validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handValidation(MethodArgumentNotValidException ex){
        Map<String, String> errors = new LinkedHashMap<>();
        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("success",false);
        payload.put("message","Validation failed");
        payload.put("errors",errors);
        return ResponseEntity.badRequest().body(payload);
    }//400


}
