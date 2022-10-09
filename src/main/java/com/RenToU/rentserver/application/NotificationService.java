package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.dto.request.UpdateNotificationDto;
import com.RenToU.rentserver.dto.service.CreateNotificationServiceDto;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;
import com.RenToU.rentserver.exceptions.MemberErrorCode;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.RenToU.rentserver.domain.ClubRole.ADMIN;
import static com.RenToU.rentserver.domain.ClubRole.OWNER;
import static com.RenToU.rentserver.domain.ClubRole.WAIT;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;

    @Transactional
    public Notification createNotification(CreateNotificationServiceDto notificationServiceDto) {
        Club club = findClub(notificationServiceDto.getClubId());
        Long writerId = notificationServiceDto.getMemberId();
        club.findClubMemberByMemberId(writerId).validateRole(true, OWNER, ADMIN);
        String title = notificationServiceDto.getTitle();
        String content = notificationServiceDto.getContent();
        String imagePath = null;
        System.out.println(notificationServiceDto.getImagePaths());
        if (!notificationServiceDto.getImagePaths().isEmpty()) {
            imagePath = notificationServiceDto.getImagePaths().get(0);
        }
        Notification notification = Notification.createNotification(title, content, imagePath, club);
        notificationRepository.save(notification);
        clubRepository.save(club);
        return notification;
    }

    public List<Notification> getMyNotifications(Long memberId) {
        Member member = findMember(memberId);
        List<Club> clubs = member.getClubListWithoutWait().stream().map(cm -> cm.getClub())
                .collect(Collectors.toList());
        List<Notification> notis = new ArrayList<>();
        clubs.stream().forEach(c -> {
            notis.addAll(c.getNotifications());
        });
        return notis;
    }

    @Transactional
    public void deleteNotification(long memberId, Long clubId, Long notificationId) {
        Club club = findClub(clubId);
        club.findClubMemberByMemberId(memberId).validateRole(true, OWNER, ADMIN);
        notificationRepository.deleteById(notificationId);
    }

    public Notification findNotification(Long id) {
        // TODO member 클럽회원 권한 체크
        return notificationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ClubErrorCode.NOTIFICATION_NOT_FOUND));
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Club findClub(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new CustomException(ClubErrorCode.CLUB_NOT_FOUND));
    }

    public List<Notification> getClubNotifications(long memberId, long clubId) {
        Club club = findClub(clubId);
        List<Notification> notifications = new ArrayList<>();
        if (club.findClubMemberByMemberId(memberId).isRole(false, WAIT)) {
            notifications = club.getNotifications();
        } else {
            notifications = club.getNotifications().stream().filter(n -> n.getIsPublic().equals(true))
                    .collect(Collectors.toList());
        }
        return notifications;
    }

    @Transactional
    public Notification updateNotification(long memberId, long clubId, long notificationId, UpdateNotificationDto dto) {
        Club club = findClub(clubId);
        club.findClubMemberByMemberId(memberId).validateRole(true, OWNER, ADMIN);
        Notification notification = findNotification(notificationId);
        boolean isPublic = false;
        if (dto.getIsPublic().contains("true")) { // equals 쓰면 에러남. dto.getIsPublic() 맨 앞 바이트에 8이 붙어있음
            isPublic = true;
        }
        notification.update(dto.getTitle(), dto.getContent(), dto.getImagePaths().get(0), isPublic);
        notificationRepository.save(notification);
        return findNotification(notification.getId());
    }
}
