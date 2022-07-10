package com.RenToU.rentserver.exceptions;

public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException(Long id){
        super("Member Not Found"+ id);
    }
}
