package com.RenToU.rentserver.exceptions;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(Long id){
        super("Item Not Found"+ id);
    }
}
