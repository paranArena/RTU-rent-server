package com.RenToU.rentserver.application;

import com.RenToU.rentserver.dto.request.UpdateNotificationDto;
import com.RenToU.rentserver.dto.service.CreateNotificationServiceDto;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.exceptions.NoUserPermissionException;
import com.RenToU.rentserver.exceptions.club.ClubNotFoundException;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.exceptions.NotificationNotFoundException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        Member writer = findMember(notificationServiceDto.getMemberId());
        club.findClubMemberByMember(writer).validateAdmin();
        String title = notificationServiceDto.getTitle();
        String content = notificationServiceDto.getContent();
        String imagePath = null;
        if(notificationServiceDto.getImagePaths()!= null) {
            imagePath = notificationServiceDto.getImagePaths().get(0);
        }
        Notification notification = Notification.createNotification(title,content, imagePath, writer,club);
        notificationRepository.save(notification);
        clubRepository.save(club);
        return notification;
    }
    public List<Notification> getMyNotifications(Long memberId){
        Member member = findMember(memberId);
        List<Club> clubs = member.getClubListWithoutWait().stream().map(cm->cm.getClub()).collect(Collectors.toList());
        List<Notification> notis = new ArrayList<>();
        clubs.stream().forEach(c->{
            notis.addAll(c.getNotifications());
        });
        return notis;
    }

    @Transactional
    public void deleteNotification(long memberId, Long clubId, Long notificationId) {
        Member member = findMember(memberId);
        Club club = findClub(clubId);
        club.findClubMemberByMember(member).validateAdmin();
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

    public List<Notification> getClubNotifications(long memberId, long clubId) {
        Club club =findClub(clubId);
        Member member = findMember(memberId);
        List<Notification> notifications = new ArrayList<>();
        if(club.findClubMemberByMember(member).isAdmin()){
            notifications = club.getNotifications();
        }else if(club.findClubMemberByMember(member).isUser()){
            notifications = club.getNotifications().stream().filter(n->n.getIsPublic() == true).collect(Collectors.toList());
        }
        else {
            throw new NoUserPermissionException(clubId);
        }
        return notifications;
    }
    @Transactional
    public Notification updateNotification(long memberId, long clubId, UpdateNotificationDto dto) {
        Member member = findMember(memberId);
        Club club = findClub(clubId);
        club.findClubMemberByMember(member).validateAdmin();
        Notification notification = findNotification(dto.getNotificationId());
        boolean isPublic = false;
        if(dto.getIsPublic() == "true"){
            isPublic = true;
        }else{
            isPublic = false;
        }
        notification.update(dto.getTitle(),dto.getContent(),dto.getImagePath().get(0),isPublic);
        notificationRepository.save(notification);
        return findNotification(notification.getId());
    }
}
