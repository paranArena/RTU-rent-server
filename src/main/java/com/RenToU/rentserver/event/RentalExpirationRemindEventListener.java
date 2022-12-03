package com.RenToU.rentserver.event;

import com.RenToU.rentserver.application.FirebaseCloudMessageService;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Rental;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Async("rentalExpirationRemind")
@Transactional
@RequiredArgsConstructor
public class RentalExpirationRemindEventListener {

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @EventListener
    public void handleRentalExpirationRemindEvent(RentalExpirationRemindEvent rentalExpirationRemindEvent) {

        // 알림 보낼 멤버 목록
        Member member = rentalExpirationRemindEvent.getMember();
        Long day = rentalExpirationRemindEvent.getDay();
        String club = rentalExpirationRemindEvent.getClub().getName();
        String product = rentalExpirationRemindEvent.getProduct().getName();
        Rental rental = rentalExpirationRemindEvent.getRental();

        // fcmToken 뽑기
        String fcmToken = member.getFcmToken();

        // 알림 보내기
        if (fcmToken != null && !fcmToken.isBlank()) {
            if (day == 1) {
                firebaseCloudMessageService.sendByToken(fcmToken, "렌탈 만료 알림",  club + "에서 빌린 " + product + "가 내일 만료됩니다!");
            }
            if (day == 0){
                firebaseCloudMessageService.sendByToken(fcmToken, "렌탈 만료 알림",  club + "에서 빌린 " + product + "가 오늘 만료됩니다!");
            }
            if (day == -1) {
                firebaseCloudMessageService.sendByToken(fcmToken, "렌탈 만료 알림", club + "에서 빌린 " + product + "가 만료되었습니다!");
            }
        }

    }

}