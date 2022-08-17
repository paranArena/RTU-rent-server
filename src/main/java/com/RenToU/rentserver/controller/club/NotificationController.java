package com.RenToU.rentserver.controller.club;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RenToU.rentserver.DTO.NotificationDTO;
import com.RenToU.rentserver.DTO.ResponseDTO;
import com.RenToU.rentserver.DTO.ResponseMessage;
import com.RenToU.rentserver.DTO.StatusCode;
import com.RenToU.rentserver.application.ClubServiceImpl;
import com.RenToU.rentserver.application.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/notifications")
public class NotificationController {
    
    private final MemberService memberService;
    private final ClubServiceImpl clubService;

    @PostMapping("")
    public ResponseEntity<?> createNotification(@PathVariable Long clubId, @Valid @RequestBody NotificationDTO notificationDTO){

        NotificationDTO resData = clubService.createNotification(clubId, memberService.getMyIdWithAuthorities(), notificationDTO);
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.UPDATE_CLUB, resData), HttpStatus.OK);
    }
}
