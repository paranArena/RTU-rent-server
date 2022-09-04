package com.RenToU.rentserver.exceptions;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(Long id) {
        super("Notification Not Found" + id);
    }
}
