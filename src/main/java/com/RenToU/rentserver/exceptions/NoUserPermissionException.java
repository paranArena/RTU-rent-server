package com.RenToU.rentserver.exceptions;

public class NoUserPermissionException extends RuntimeException {
    public NoUserPermissionException(Long clubId){
        super("No Permission for club"+ clubId);
    }
}
