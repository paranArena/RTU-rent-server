package com.RenToU.rentserver.exceptions;

public class CannotRentException extends RuntimeException {
    public CannotRentException(Long itemId) {
        super("cannot Rent" + itemId);
    }
}
