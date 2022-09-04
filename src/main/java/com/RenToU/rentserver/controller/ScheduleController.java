package com.RenToU.rentserver.controller;

import com.RenToU.rentserver.application.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private ScheduleService scheduleService;

    @PostMapping("/rental/wait")
    public ResponseEntity<Void> checkExpiredRentalWait() {
        scheduleService.checkExpiredRentalWait();
        return ResponseEntity.ok().build();
    }
}
