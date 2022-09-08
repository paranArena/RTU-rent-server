package com.RenToU.rentserver.handler;

import com.RenToU.rentserver.dto.ErrorResponse;
import com.RenToU.rentserver.exceptions.CannotRentException;
import com.RenToU.rentserver.exceptions.NoAdminPermissionException;
import com.RenToU.rentserver.exceptions.NotClubRoleWaitingException;
import com.RenToU.rentserver.exceptions.RentalNotFoundException;
import com.RenToU.rentserver.exceptions.club.CannotGrantException;
import com.RenToU.rentserver.exceptions.club.CannotJoinClubException;
import com.RenToU.rentserver.exceptions.club.ClubNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RentErrorAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CannotRentException.class)
    public ErrorResponse canntoRent(CannotRentException ex) {
        return new ErrorResponse(ex.getMessage());
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RentalNotFoundException.class)
    public ErrorResponse canntoRent(RentalNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

}
