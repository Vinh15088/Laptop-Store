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
    PASSWORD_INVALID("Password must be at least 8 characters", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_REGISTER),
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
    CATEGORY_NAME_EXISTED("Category's name is existed", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_CATEGORY),

    PRODUCT_NAME_NOT_BLANK("Product's name is not blank", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_PRODUCT),
    PRODUCT_SKU_NOT_BLANK("SKU is not blank", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_PRODUCT),
    PRODUCT_STOCK_NOT_NULL("STOCK is not blank", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_PRODUCT),
    PRODUCT_NAME_EXISTED("Product's name existed", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_PRODUCT),
    PRODUCT_STOCK_MIN_0("Product's stock must be at least 0", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_PRODUCT),
    PRODUCT_NOT_FOUND("Product not found", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_PRODUCT),

    ORDER_NOT_FOUND("Order not found", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_ORDER),
    ORDER_ADDRESS_NOT_BLANK("Order's address not blank", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_ORDER),
    ORDER_PHONE_NOT_BLANK("Order's phone not blank", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_ORDER),
    ORDER_PAYMENT_TYPE_NOT_BLANK("Order's payment type not blank", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_ORDER),
    ORDER_DETAIL_NOT_EMPTY("Order's detail type not empty", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_ORDER),

    STATUS_ORDER_NOT_FOUND("Status order not found", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_ORDER),
    UPDATE_ORDER_STATUS_FAIL("Update order status fail", HttpStatus.BAD_REQUEST, ErrorCode.ERROR_ORDER),

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
