package com.RenToU.rentserver.controller.club;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.RenToU.rentserver.dto.request.UpdateNotificationDto;
import com.RenToU.rentserver.dto.response.NotificationDto;
import com.RenToU.rentserver.application.NotificationService;
import com.RenToU.rentserver.domain.Notification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.CreateNotificationDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.response.preview.NotificationPreviewDto;
import com.RenToU.rentserver.dto.service.CreateNotificationServiceDto;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/notifications")
public class ClubNotificationController {

    private final MemberService memberService;
    private final NotificationService notificationService;
    private final Mapper mapper;

    @PostMapping("")
    public ResponseEntity<?> createNotification(@PathVariable long clubId,
            @Valid @RequestBody CreateNotificationDto createNotificationDto) {
        long memberId = memberService.getMyIdWithAuthorities();
        CreateNotificationServiceDto notificationServiceDto = mapper.map(createNotificationDto,
                CreateNotificationServiceDto.class);
        notificationServiceDto.setMemberId(memberId);
        notificationServiceDto.setClubId(clubId);
        Notification notification = notificationService.createNotification(notificationServiceDto);

        NotificationDto resData = NotificationDto.from(notification);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_NOTIFICATION, resData));
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<?> getNotification(@PathVariable long clubId, @PathVariable long notificationId) {
        // long memberId = memberService.getMyIdWithAuthorities();
        Notification notification = notificationService.findNotification(notificationId);
        NotificationDto resData = NotificationDto.from(notification);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_NOTIFICATION, resData));
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<?> updateNotification(@PathVariable long clubId, @PathVariable long notificationId,
            @Valid @RequestBody UpdateNotificationDto updateNotificationDto) {
        long memberId = memberService.getMyIdWithAuthorities();
        Notification notification = notificationService.updateNotification(memberId, clubId, notificationId,
                updateNotificationDto);
        NotificationDto resData = NotificationDto.from(notification);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.UPDATE_NOTIFICATION, resData));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable long clubId, @PathVariable long notificationId) {
        long memberId = memberService.getMyIdWithAuthorities();
        notificationService.deleteNotification(memberId, clubId, notificationId);

        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.DELETE_NOTIFICATION));
    }

    // Deprecated예정
    @GetMapping("/search/all")
    public ResponseEntity<?> searchNotificationsByClub(@PathVariable long clubId) {
        long memberId = memberService.getMyIdWithAuthorities();
        List<Notification> notifications = notificationService.getClubNotifications(memberId, clubId);
        List<NotificationPreviewDto> resData = notifications.stream()
                .map(n -> NotificationPreviewDto.from(n))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_NOTIFICATION_SUCCESS, resData));
    }

    @PostMapping("/{notificationId}/report")
    public ResponseEntity<?> reportMember(@PathVariable long clubId, @PathVariable long notificationId) {
        // TODO: process POST request

        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.REPORT_NOTIFICATION));
    }
}
