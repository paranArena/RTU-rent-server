package com.RenToU.rentserver.exceptions.club;

public class ClubNotFoundException extends RuntimeException{
    public ClubNotFoundException(Long id){
        super("Club Not Found"+ id);
    }
    public ClubNotFoundException(String name){
        super("Club Not Found"+ name);
    }
}
