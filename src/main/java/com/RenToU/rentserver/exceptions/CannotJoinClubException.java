package com.RenToU.rentserver.exceptions;

public class CannotJoinClubException extends RuntimeException {
    public CannotJoinClubException(Long id, String name) {
        super("cannot join club" + name + "(" + id + ")");
    }
}
