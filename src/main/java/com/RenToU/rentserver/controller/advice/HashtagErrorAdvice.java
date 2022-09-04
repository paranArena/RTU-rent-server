package com.RenToU.rentserver.controller.advice;
import com.RenToU.rentserver.dto.ErrorResponse;
import com.RenToU.rentserver.exceptions.HashtagNotFoundException;
import com.RenToU.rentserver.exceptions.club.ClubNotFoundException;
import com.RenToU.rentserver.exceptions.club.CannotJoinClubException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HashtagErrorAdvice {
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HashtagNotFoundException.class)
    public ErrorResponse HashtagNotFound(HashtagNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
