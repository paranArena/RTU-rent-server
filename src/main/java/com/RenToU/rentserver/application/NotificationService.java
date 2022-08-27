package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.dto.service.NotificationServiceDto;
import com.RenToU.rentserver.exceptions.ClubNotFoundException;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    @Transactional
    public Notification createNotification(NotificationServiceDto notificationServiceDto) {
         Club club = findClub(notificationServiceDto.getClubId());
         Member writer = findMember(notificationServiceDto.getMemberId());
         club.findClubMemberByMember(writer).validateAdmin();
         Notification notification = Notification.createNotification(notificationServiceDto.getTitle(),notificationServiceDto.getContent(),writer,club);
         clubRepository.save(club);
         return notification;
    }

    private Member findMember(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    private Club findClub(Long id){
        return clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException(id));
    }
}
