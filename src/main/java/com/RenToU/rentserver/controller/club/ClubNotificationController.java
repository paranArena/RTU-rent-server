package com.RenToU.rentserver.controller.club;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.RenToU.rentserver.dto.response.NotificationDto;
import com.RenToU.rentserver.application.NotificationService;
import com.RenToU.rentserver.application.S3Service;
import com.RenToU.rentserver.domain.Notification;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.RenToU.rentserver.application.ClubService;
import com.RenToU.rentserver.application.ClubService;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.CreateNotificationDto;
import com.RenToU.rentserver.dto.response.NotificationDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.response.preview.NotificationPreviewDto;
import com.RenToU.rentserver.dto.service.NotificationServiceDto;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/notifications")
public class ClubNotificationController {
    
    private final MemberService memberService;
    private final ClubService clubService;
    private final S3Service s3Service;
    private final NotificationService notificationService;
    private final Mapper mapper;

    @PostMapping("")
    public ResponseEntity<?> createNotification(@PathVariable long clubId, @ModelAttribute CreateNotificationDto createNotificationDto) throws IOException{
        long memberId = memberService.getMyIdWithAuthorities();
        List<MultipartFile> images = createNotificationDto.getImage();
        List<String> imagePaths = new ArrayList<>();
        if(images.size() != 0){
            imagePaths.addAll(
                images.stream().map((img)->{
                    try {
                        return s3Service.upload(img);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList())
                );
        }

        NotificationServiceDto notificationServiceDto = mapper.map(createNotificationDto, NotificationServiceDto.class);
        notificationServiceDto.setImagePaths(imagePaths);
        notificationServiceDto.setMemberId(memberId);
        notificationServiceDto.setClubId(clubId);
        Notification notification = notificationService.createNotification(notificationServiceDto);
        
        NotificationDto resData = NotificationDto.from(notification);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_NOTIFICATION, resData));
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<?> getNotification(@PathVariable long clubId, @PathVariable long notificationId){
        long memberId = memberService.getMyIdWithAuthorities();
        Notification notification = notificationService.findNotification(notificationId);
        NotificationDto resData = NotificationDto.from(notification);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_NOTIFICATION, resData));
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<?> updateNotification(@PathVariable long clubId, @PathVariable long notificationId){
        long memberId = memberService.getMyIdWithAuthorities();
        // notificationService.updateNotification(memberId, notificationId);
        return null;
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable long clubId, @PathVariable long notificationId){
        long memberId = memberService.getMyIdWithAuthorities();
        notificationService.deleteNotification(memberId, notificationId);

        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.DELETE_NOTIFICATION));
    }

    @GetMapping("/search/all")
    public ResponseEntity<?> searchNotificationsAll(@PathVariable long clubId){
        List<Notification> notifications = clubService.findClubById(clubId).getNotifications();
        List<NotificationPreviewDto> resData =
            notifications.stream()
            .map(n->NotificationPreviewDto.from(n))
            .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_NOTIFICATION_SUCCESS, resData));
    }
    //TODO searh notification
    // @GetMapping("/search")
    // public ResponseEntity<?> searchNotifications(@PathVariable long clubId, @RequestParam Map<String,String> searchMap){
    //     List<Notification> notifications = clubService.findClubById(clubId).getNotifications();
    //     if(searchMap.containsKey("title")){
    //         String notificationTitle = searchMap.get("title");
    //         Notification notification = notificationService.findNotificationByTitle(notificationTitle);
    //         NotificationPreviewDto resData = NotificationPreviewDto.from(notification);
    //         return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_SUCCESS, resData));
    //     }

    //     return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_NOTIFICATION_FAIL));
    // }
}
