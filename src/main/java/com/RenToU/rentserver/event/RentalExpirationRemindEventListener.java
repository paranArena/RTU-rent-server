package com.RenToU.rentserver.event;

import com.RenToU.rentserver.application.FirebaseCloudMessageService;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Rental;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        String club = rentalExpirationRemindEvent.getClub().getName();
        String product = rentalExpirationRemindEvent.getProduct().getName();
        Rental rental = rentalExpirationRemindEvent.getRental();

        // fcmToken 뽑기
        String fcmToken = member.getFcmToken();

        // 알림 보내기
        if (fcmToken != null && !fcmToken.isBlank()) {
            firebaseCloudMessageService.sendByToken(fcmToken, "Ren2U", "내일 렌탈 만료 물품: (" + club + ") " + product);
        }

    }

}