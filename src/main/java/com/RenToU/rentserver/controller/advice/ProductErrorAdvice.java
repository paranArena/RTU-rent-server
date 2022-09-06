package com.RenToU.rentserver.controller.advice;

import com.RenToU.rentserver.exceptions.ItemAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.RenToU.rentserver.dto.ErrorResponse;
import com.RenToU.rentserver.exceptions.ItemNotFoundException;
import com.RenToU.rentserver.exceptions.ProductNotFoundException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductErrorAdvice {
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse productNotFound(ProductNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ItemNotFoundException.class)
    public ErrorResponse itemNotFound(ItemNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ItemAlreadyExistException.class)
    public ErrorResponse ItemAlreadyExist() {
        return new ErrorResponse("numbering이 이미 존재합니다.");
    }
}
