package com.RenToU.rentserver.controller.club;

import javax.validation.Valid;

import com.RenToU.rentserver.dto.response.NotificationDto;
import com.RenToU.rentserver.application.NotificationService;
import com.RenToU.rentserver.domain.Notification;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.CreateNotificationDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.service.NotificationServiceDto;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/notifications")
public class NotificationController {
    
    private final MemberService memberService;
    private final NotificationService notificationService;
    private final Mapper mapper;

    @PostMapping("")
    public ResponseEntity<?> createNotification(@PathVariable Long clubId, @Valid @RequestBody CreateNotificationDto createNotificationDto){
        long memberId = memberService.getMyIdWithAuthorities();
        NotificationServiceDto notificationServiceDto = mapper.map(createNotificationDto, NotificationServiceDto.class);
        notificationServiceDto.setMemberId(memberId);
        notificationServiceDto.setClubId(clubId);

        Notification notification = notificationService.createNotification(notificationServiceDto);
        return new ResponseEntity<>(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_NOTIFICATION, NotificationDto.from(notification)), HttpStatus.OK);
    }
}
