package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubHashtag;
import com.RenToU.rentserver.domain.Hashtag;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class NotificationRepositoryTest {
    @Autowired
    private NotificationRepository repository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ClubRepository clubRepository;
    Member member;
    Club club1;
    Club club2;
    Hashtag hashtag1;

    @BeforeEach
    void setup() {
        member = memberRepository.findById(1L).get();
        club1 = Club.createClub("TestClub", "this is Test", "www.com", member, new ArrayList<>());
        club2 = Club.createClub("TestClub2", "this is Test2", "www.com", member, new ArrayList<>());
        hashtag1 = Hashtag.createHashtag("hash1");
    }

    @Test
    @DisplayName("notification이 저장 될 시 id, created_at, updated_at이 자동 생성됨")
    void notificationSave() {
        // given
        Notification notification = Notification.createNotification("Test", "content", null, club1);
        repository.save(notification);
        // when

        // then
        assertThat(notification.getId()).isEqualTo(1L);
        assertThat(notification.getCreatedAt()).isNotNull();

    }

}