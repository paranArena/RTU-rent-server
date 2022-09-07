package com.RenToU.rentserver.handler;

import com.RenToU.rentserver.dto.ErrorResponse;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.exceptions.MemberNotActivatedException;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.exceptions.NotAjouEmailException;
import com.RenToU.rentserver.exceptions.WrongEmailCodeException;
import com.RenToU.rentserver.exceptions.club.CannotJoinClubException;
import com.RenToU.rentserver.exceptions.club.ClubNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberErrorAdvice {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(WrongEmailCodeException.class)
    public ErrorResponse WrongEmailCode() {
        return new ErrorResponse("인증번호가 틀렸습니다.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ClubNotFoundException.class)
    public ErrorResponse ClubNotFound(ClubNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MemberNotFoundException.class)
    public ErrorResponse MemberNotFound(MemberNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotAjouEmailException.class)
    public ErrorResponse NotAjouEmail() {
        return new ErrorResponse("아주대 이메일이 아닙니다.");
    }

    @ExceptionHandler(MemberNotActivatedException.class)
    public ResponseEntity<?> MemberNotActivated(MemberNotActivatedException ex) {
        return ResponseEntity.ok(ResponseDto.res(StatusCode.UNAUTHORIZED, ex.getMessage()));
    }
}
