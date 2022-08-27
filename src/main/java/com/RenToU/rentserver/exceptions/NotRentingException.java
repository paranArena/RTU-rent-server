package com.RenToU.rentserver.exceptions;

public class NotRentingException extends RuntimeException{
    public NotRentingException(Long id){
        super("You are Not Renting" + id);
    }
}
