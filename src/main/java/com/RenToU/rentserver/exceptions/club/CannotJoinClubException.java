package com.RenToU.rentserver.exceptions.club;

public class CannotJoinClubException extends RuntimeException {
    public CannotJoinClubException(Long id, String name,String msg) {
        super("cannot join club" + name + "(" + id + ") "+ msg);
    }
}
