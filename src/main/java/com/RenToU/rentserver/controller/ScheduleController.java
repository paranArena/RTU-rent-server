package com.RenToU.rentserver.controller;

import com.RenToU.rentserver.application.ScheduleService;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private ScheduleService scheduleService;

    @PostMapping("/rental/wait")
    public ResponseEntity<?> checkExpiredRentalWait(@RequestHeader("scretKey") String secretKey) {
        if (!secretKey.equals("paranajou2022-2")) {
            return ResponseEntity.badRequest().build();
        }
        scheduleService.checkExpiredRentalWait();
        return ResponseEntity.ok(ResponseDto.res(StatusCode.ACCEPT, ResponseMessage.CHECK_EXPIRED_RENTAL_WAIT));
    }
}
