package com.RenToU.rentserver.exceptions;

public class NotWaitingException extends RuntimeException{
    public NotWaitingException(Long id){
        super("Rental Not Found" + id);
    }
}
