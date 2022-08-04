package com.RenToU.rentserver.controller;

import com.RenToU.rentserver.DTO.*;
import com.RenToU.rentserver.application.ClubServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubServiceImpl clubService;

    public ClubController(ClubServiceImpl clubService) {
        this.clubService = clubService;
    }

    @PostMapping("")
    public ResponseEntity<?> signup(@Valid @RequestBody ClubDTO clubDTO) {

        clubService.createClub(1L, clubDTO.getName(), clubDTO.getIntroduction());
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.LOGIN_SUCCESS, null), HttpStatus.OK);
    }
}
