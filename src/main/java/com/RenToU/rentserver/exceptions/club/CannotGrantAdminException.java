package com.RenToU.rentserver.exceptions.club;

public class CannotGrantAdminException extends RuntimeException {
    public CannotGrantAdminException(Long id, String name, String msg) {
        super("cannot grant Admin" + name + "(" + id + ") "+ msg);
    }
}
