package com.RenToU.rentserver.exceptions;

public class RentalNotFoundException extends RuntimeException {
    public RentalNotFoundException() {
        super("Rental Not Found");
    }

    public RentalNotFoundException(Long id) {
        super("Rental Not Found" + id);
    }
}
