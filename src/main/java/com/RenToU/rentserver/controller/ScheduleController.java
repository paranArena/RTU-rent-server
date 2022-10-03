package com.RenToU.rentserver.controller;

import com.RenToU.rentserver.application.ScheduleService;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @DeleteMapping("/rental/wait")
    public ResponseEntity<?> checkExpiredRentalWait(@RequestHeader("secretKey") String secretKey) {
        if (!secretKey.equals("ajouparan2022-2")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        scheduleService.checkExpiredRentalWait();
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CHECK_EXPIRED_RENTAL_WAIT));
    }
    @PostMapping("/join/ren2u/all")
    public ResponseEntity<?> joinRen2UAll(@RequestHeader("secretKey") String secretKey) {
        if (!secretKey.equals("ajouparan2022-2")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        scheduleService.joinRen2UAll();
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, "렌트유 가입 성공"));
    }
}
