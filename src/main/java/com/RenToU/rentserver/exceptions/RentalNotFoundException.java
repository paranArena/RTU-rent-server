package com.RenToU.rentserver.exceptions;

public class RentalNotFoundException extends RuntimeException{
    public RentalNotFoundException(){
        super("Rental Not Found");
    }
}
