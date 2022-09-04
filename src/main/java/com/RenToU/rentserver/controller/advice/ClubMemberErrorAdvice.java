package com.RenToU.rentserver.controller.advice;
import com.RenToU.rentserver.dto.ErrorResponse;
import com.RenToU.rentserver.exceptions.club.ClubNotFoundException;
import com.RenToU.rentserver.exceptions.club.CannotJoinClubException;
import com.RenToU.rentserver.exceptions.clubMember.ClubMemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClubMemberErrorAdvice {
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ClubMemberNotFoundException.class)
    public ErrorResponse CannotFindClubMember(ClubMemberNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

}
