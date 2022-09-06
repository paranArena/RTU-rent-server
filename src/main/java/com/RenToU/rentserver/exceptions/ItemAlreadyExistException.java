package com.RenToU.rentserver.exceptions;

public class ItemAlreadyExistException extends RuntimeException{
    public ItemAlreadyExistException(){
        super("Item Already Exist");
    }
}
