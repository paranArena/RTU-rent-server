package com.RenToU.rentserver.exceptions;

public class NoOwnerPermissionException extends RuntimeException {
    public NoOwnerPermissionException(Long clubId){
        super("No Permission for club"+ clubId);
    }
}
