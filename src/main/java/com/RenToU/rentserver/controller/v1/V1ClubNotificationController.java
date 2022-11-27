package com.RenToU.rentserver.controller.v1;

import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.NotificationService;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.V1CreateNotificationDto;
import com.RenToU.rentserver.dto.request.V1UpdateNotificationDto;
import com.RenToU.rentserver.dto.response.NotificationDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.service.CreateNotificationServiceDto;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/clubs/{clubId}/notifications")
public class V1ClubNotificationController {

    private final MemberService memberService;
    private final NotificationService notificationService;
    private final Mapper mapper;

    @PostMapping("")
    public ResponseEntity<?> createNotification(@PathVariable long clubId,
                                                @Valid @RequestBody V1CreateNotificationDto createNotificationDto) {
        long memberId = memberService.getMyIdWithAuthorities();
        CreateNotificationServiceDto notificationServiceDto = mapper.map(createNotificationDto,
                CreateNotificationServiceDto.class);
        notificationServiceDto.setMemberId(memberId);
        notificationServiceDto.setClubId(clubId);
        Notification notification = notificationService.createNotification(notificationServiceDto);

        NotificationDto resData = NotificationDto.from(notification);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_NOTIFICATION, resData));
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<?> updateNotification(@PathVariable long clubId, @PathVariable long notificationId,
                                                @Valid @RequestBody V1UpdateNotificationDto updateNotificationDto) {
        long memberId = memberService.getMyIdWithAuthorities();
        Notification notification = notificationService.updateNotification(memberId, clubId, notificationId,
                updateNotificationDto);
        NotificationDto resData = NotificationDto.from(notification);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.UPDATE_NOTIFICATION, resData));
    }
}
