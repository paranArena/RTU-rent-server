package com.RenToU.rentserver.controller.advice;
import com.RenToU.rentserver.dto.ErrorResponse;
import com.RenToU.rentserver.exceptions.club.ClubNotFoundException;
import com.RenToU.rentserver.exceptions.club.CannotJoinClubException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClubErrorAdvice {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CannotJoinClubException.class)
    public ErrorResponse CannotJoinClub(CannotJoinClubException ex){
        return new ErrorResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ClubNotFoundException.class)
    public ErrorResponse ClubNotFound(ClubNotFoundException ex){
        return new ErrorResponse(ex.getMessage());
    }
}
