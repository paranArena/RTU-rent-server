package com.RenToU.rentserver.exceptions;

public class NotInRangeException extends RuntimeException{
    public NotInRangeException(Double distance){
        super("Not in Range your distance is" + distance);
    }
}
