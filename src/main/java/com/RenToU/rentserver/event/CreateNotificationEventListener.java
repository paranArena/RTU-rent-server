package com.RenToU.rentserver.event;

import com.RenToU.rentserver.application.FirebaseCloudMessageService;
import com.RenToU.rentserver.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@Async("createNotification")
@Transactional
@RequiredArgsConstructor
public class CreateNotificationEventListener {

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @EventListener
    public void handleMatchingCompleteEvent(CreateNotificationEvent createNotificationEvent) {

        // 알림 보낼 멤버 목록
        List<Member> memberList = createNotificationEvent.getMemberList();

        // fcmToken 뽑기
        List<String> fcmTokenList = memberList
                .stream()
                .map(Member::getFcmToken)
                .filter(fcmToken -> fcmToken != null && !fcmToken.isBlank()).collect(toList());

        // 알림 보내기
        if (fcmTokenList.size() != 0) {
            firebaseCloudMessageService.sendByTokenList(fcmTokenList);
        }

    }
}