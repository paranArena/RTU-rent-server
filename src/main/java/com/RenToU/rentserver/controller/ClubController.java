package com.RenToU.rentserver.controller;

import com.RenToU.rentserver.DTO.*;
import com.RenToU.rentserver.application.ClubServiceImpl;
import com.RenToU.rentserver.application.LoginService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    private final ClubServiceImpl clubService;
    private final LoginService loginService;

    @PostMapping("")
    public ResponseEntity<?> signup(@Valid @RequestBody ClubDTO clubDTO) {

        long memberId = loginService.getMyUserWithAuthorities().getId();
        clubService.createClub(memberId, clubDTO.getName(), clubDTO.getThumbnailPath(),clubDTO.getIntroduction());
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.CREATED_CLUB, null), HttpStatus.OK);
    }
}
