package com.RenToU.rentserver.exceptions;

public class NoAdminPermissionException extends RuntimeException {
    public NoAdminPermissionException(Long clubId){
        super("No Permission for club"+ clubId);
    }
}
