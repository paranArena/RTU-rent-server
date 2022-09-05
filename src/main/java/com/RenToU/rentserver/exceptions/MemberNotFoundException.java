package com.RenToU.rentserver.exceptions;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String email) {
        super("Member Not Found " + email);
    }

    public MemberNotFoundException(Long id) {
        super("Member Not Found" + id);
    }
}
