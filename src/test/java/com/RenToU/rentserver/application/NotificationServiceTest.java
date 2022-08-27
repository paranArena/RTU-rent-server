package com.RenToU.rentserver.application;

import com.RenToU.rentserver.dto.service.NotificationServiceDto;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.exceptions.NoAdminPermissionException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isInstanceOf;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @InjectMocks
    private NotificationService service;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private ClubRepository clubRepository;

    private static String INITIAL_NOTI_TITLE = "NEW NOTIFICATION";
    private static String INITIAL_NOTI_CONTENT = "공지사항 내용입니다.";
    @Test
    @DisplayName("공지사항이 제대로 등록됩니다.")
    void createNotification(){
        //given
        Member member = Member.createMember("test","test@test.com");
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(member));

        Club club = Club.createClub("ClubName","ClubIntro","ClubThumb",member,new ArrayList<>());
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        //when
        NotificationServiceDto dto = new NotificationServiceDto();
        dto.setClubId(1L);
        dto.setMemberId(1L);
        dto.setTitle(INITIAL_NOTI_TITLE);
        dto.setContent(INITIAL_NOTI_CONTENT);
        Notification notification = service.createNotification(dto);
        //then
        assertThat(notification.getTitle()).isEqualTo(INITIAL_NOTI_TITLE);
        assertThat(notification.getContent()).isEqualTo(INITIAL_NOTI_CONTENT);
        assertThat(notification.getWriter()).isEqualTo(member);
        assertThat(notification.getClub()).isEqualTo(club);
    }
    @Test
    @DisplayName("그룹에 가입되지 않은 유저가 공지사항을 작성하면 NoAdminPermissionException 발생.")
    void notJoiningUserWriteNotification(){
        //given
        Member admin = Member.createMemberWithId(1L,"test","test@test.com");
        Member writer = Member.createMemberWithId(2L,"writer","writer@test.com");
        when(memberRepository.findById(2L)).thenReturn(Optional.ofNullable(writer));

        Club club = Club.createClub("ClubName","ClubIntro","ClubThumb",admin,new ArrayList<>());
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        NotificationServiceDto dto = new NotificationServiceDto();
        dto.setClubId(1L);
        dto.setMemberId(2L);
        dto.setTitle(INITIAL_NOTI_TITLE);
        dto.setContent(INITIAL_NOTI_CONTENT);
        //when,then

        assertThatThrownBy(() -> service.createNotification(dto))
                .isInstanceOf(MemberNotFoundException.class);

    }
    @Test
    @DisplayName("그룹 관리자가 아니면 일반 유저면 NoAdminPermissionException 발생.")
    void noPermissionForWritingNotification(){
        //given
        Member admin = Member.createMemberWithId(1L,"test","test@test.com");
        Member writer = Member.createMemberWithId(2L,"writer","writer@test.com");
        when(memberRepository.findById(2L)).thenReturn(Optional.ofNullable(writer));

        Club club = Club.createClub("ClubName","ClubIntro","ClubThumb",admin,new ArrayList<>());
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        club.addClubMember(ClubMember.createClubMember(club,writer, ClubRole.USER));
        NotificationServiceDto dto = new NotificationServiceDto();
        dto.setClubId(1L);
        dto.setMemberId(2L);
        dto.setTitle(INITIAL_NOTI_TITLE);
        dto.setContent(INITIAL_NOTI_CONTENT);
        //when,then
        assertThatThrownBy(() -> service.createNotification(dto))
                .isInstanceOf(NoAdminPermissionException.class);

    }

}