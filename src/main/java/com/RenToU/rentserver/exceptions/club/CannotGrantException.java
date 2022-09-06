package com.RenToU.rentserver.exceptions.club;

public class CannotGrantException extends RuntimeException {
    public CannotGrantException(Long id, String name, String msg) {
        super("cannot grant role" + name + "(" + id + ") " + msg);
    }
}
