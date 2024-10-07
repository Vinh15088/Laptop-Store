package com.LaptopWeb.exception;

import com.LaptopWeb.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ExceptionHanlder {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<?>> handlingAppException(AppException e) {
        ErrorApp errorApp = e.getErrorApp();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(false)
                .code(errorApp.getErrorCode().getCode())
                .message(errorApp.getMessage())
                .build();

        return ResponseEntity.status(errorApp.getHttpStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handlingMethodArgumentNotValidException(
            MethodArgumentNotValidException exception){

        String enumKey = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();

        ErrorApp errorApp = ErrorApp.valueOf(enumKey);

        ApiResponse<?> apiResponse =ApiResponse.builder()
                .success(false)
                .code(errorApp.getErrorCode().getCode())
                .message(errorApp.getMessage())
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }


    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<?>> handlingAppException(Exception e) {

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .success(false)
                .message(e.getMessage())
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
