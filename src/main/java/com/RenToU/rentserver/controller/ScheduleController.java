package com.RenToU.rentserver.controller;

import com.RenToU.rentserver.application.ScheduleService;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/message/rentalExpirationRemind")
    public ResponseEntity<?> messageRentalExpirationRemind(@RequestHeader("secretKey") String secretKey) {
        if (!secretKey.equals("ajouparan2022-2")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        scheduleService.checkExpiredRental(1);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, "내일 만료되는 렌탈 리마인드 메세지 전송 성공"));
    }
}
