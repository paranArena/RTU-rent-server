package com.RenToU.rentserver.exceptions;

public class WrongEmailCodeException extends RuntimeException {
    public WrongEmailCodeException(){
        super("code is not same");
    }
}
