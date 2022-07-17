package com.RenToU.rentserver.exceptions;

public class NotWaitingJoinException extends RuntimeException {
    public NotWaitingJoinException(Long clubId){
        super("Not Waiting join for club"+ clubId);
    }
}
