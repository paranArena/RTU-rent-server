package com.RenToU.rentserver.application;

import com.RenToU.rentserver.dto.service.NotificationServiceDto;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.dto.service.NotificationServiceDto;
import com.RenToU.rentserver.exceptions.ClubNotFoundException;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.exceptions.NotificationNotFoundException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    @Transactional
    public Notification createNotification(NotificationServiceDto notificationServiceDto) {
        Club club = findClub(notificationServiceDto.getClubId());
        Member writer = findMember(notificationServiceDto.getMemberId());
        club.findClubMemberByMember(writer).validateAdmin();
        String title = notificationServiceDto.getTitle();
        String content = notificationServiceDto.getContent();
        Notification notification = Notification.createNotification(title,content,writer,club);
        notificationRepository.save(notification);
        clubRepository.save(club);
        return notification;
    }

    @Transactional
    public void deleteNotification(long memberId, Long notificationId) {
        //TODO member 클럽장 권한 체크
        notificationRepository.deleteById(notificationId);
    }

    public Notification findNotification(Long id) {
        //TODO member 클럽회원 권한 체크
        return notificationRepository.findById(id)
                .orElseThrow(()->new NotificationNotFoundException(id));
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
