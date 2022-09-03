package com.RenToU.rentserver.exceptions.club;

public class CannotCancelJoinClubException extends RuntimeException{
    public CannotCancelJoinClubException(Long id, String name,String msg) {
        super("cannot cancel join club" + name + "(" + id + ") "+ msg);
    }
}
