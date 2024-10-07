package com.LaptopWeb.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorApp {
    USERNAME_NOT_EXISTED("Username is not exist", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_LOGIN),
    PASSWORD_INCORRECT("Password is incorrect", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_REGISTER),
    USERNAME_INVALID("Username must be at least 5 characters", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_REGISTER),
    PASSWORD_INVALID("Password must be at least 8 charactes", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_REGISTER),
    FULLNAME_INVALID("Full name is required", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_REGISTER),
    EMAIL_INVALID("Email should be valid", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_REGISTER),
    USERNAME_EXISTED("User name existed", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_REGISTER),
    EMAIL_EXISTED("Email existed", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_REGISTER),
    USER_NOTFOUND("User not found", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_USER),
    TOKEN_INVALID("Token Authenticated Fail", HttpStatus.UNAUTHORIZED, ErrorCode.ERROR_AUTHENTICATION),
    UNAUTHENTICATED("Unauthenticated", HttpStatus.UNAUTHORIZED, ErrorCode.ERROR_AUTHENTICATION),

    BRAND_NAME_EXISTED("Brand name existed", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_BRAND),
    BRAND_NAME_EMPTY("Brand's name is not null", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_BRAND),
    BRAND_LOGO_EMPTY("Brand's logo is not null", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_BRAND),
    BRAND_NOT_FOUND("Brand not found", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_BRAND),

    CATEGORY_NAME_EMPTY("Category's name is not null", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_CATEGORY),
    CATEGORY_NOT_FOUND("Category not found", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_CATEGORY),
    CATEGORY_NAME_EXISTED("Category's name is existed", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_CATEGORY)
    ;


    private String message;
    private HttpStatusCode httpStatusCode;
    private ErrorCode errorCode;

    ErrorApp(String message, HttpStatusCode httpStatusCode, ErrorCode errorCode) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
    }


}
