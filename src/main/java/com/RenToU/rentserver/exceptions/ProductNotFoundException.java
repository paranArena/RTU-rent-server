package com.RenToU.rentserver.exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long id){
        super("Product Not Found"+ id);
    }
}
