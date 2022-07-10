package com.RenToU.rentserver.exceptions;

public class ClubNotFoundException extends RuntimeException{
    public ClubNotFoundException(Long id){
        super("Club Not Found"+ id);
    }
}
