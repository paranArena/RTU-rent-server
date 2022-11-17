package com.RenToU.rentserver.controller.util;

import com.RenToU.rentserver.application.FirebaseCloudMessageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PushController {

    private final FirebaseCloudMessageService firebaseCloudMessageService;
    @PostMapping("/api/fcm/register")
    public ResponseEntity registerFcmToken(@RequestBody RegisterDto registerDto) throws IOException {
        System.out.println(registerDto.getMemberId() + " "
                +registerDto.getFcmToken());
        firebaseCloudMessageService.setFcmToken(registerDto.getMemberId(),registerDto.getFcmToken());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(@RequestBody RequestDto requestDto) throws IOException {
        System.out.println(requestDto.getTargetToken() + " "
                +requestDto.getTitle() + " " + requestDto.getBody());

        firebaseCloudMessageService.sendMessageTo(
                requestDto.getTargetToken(),
                requestDto.getTitle(),
                requestDto.getBody());
        return ResponseEntity.ok().build();
    }
}

@Data
class RequestDto{
    private String title;
    private String targetToken;
    private String body;

}
@Data
class RegisterDto{
    private Long memberId;
    private String fcmToken;

}