package com.example.demo.common.exception;
//chuc nang quan ly va xu ly loi tap trung cho toan bo project

import com.example.demo.common.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
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
    //.badRequest() khi muốn trả về mã 400 , httpstatus la tra ve 400,404
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
    }//tra ve 404
    //ham xu ly loi BadRequesException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(payload);
    }//400
    //bat loi dang nhap sai
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleCreadential(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Username or password is invalid"));
    }
    //kiem tra tai khoan mat khau(cap quyen)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Access denied"));
    }
    //kiem tra tinh hop le cua du lieu
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("Data Integrity violation. please check duplicate or foreign key values."));
    }
    //ham kiem tra server
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnkown(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error" + ex.getMessage()));
    }

}
