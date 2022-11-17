package com.RenToU.rentserver.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubHashtag;
import com.RenToU.rentserver.domain.Hashtag;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.infrastructure.ClubHashtagRepository;
import com.RenToU.rentserver.infrastructure.HashtagRepository;

@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {
    @InjectMocks
    private HashtagService service;
    @Mock
    private HashtagRepository hashtagRepository;
    @Mock
    private ClubHashtagRepository clubHashtagRepository;

    private static String INITIAL_NOTI_TITLE = "NEW NOTIFICATION";
    private static String INITIAL_NOTI_CONTENT = "공지사항 내용입니다.";

    @Test
    @DisplayName("해싀태그로 그룹 목록이 검색됩니다.")
    void createNotification() {
        // given
        Hashtag hashtag = Hashtag.createHashtag("testHash");
        Member member = Member.createMember("test", "test@test.com");
        Club club = Club.createClub("ClubName", "ClubIntro", "ClubThumb", member, List.of(hashtag));
        Club club2 = Club.createClub("ClubName2", "ClubIntro2", "ClubThumb2", member, List.of(hashtag));
        ClubHashtag clubHashtag1 = ClubHashtag.createClubHashtag(club, hashtag);
        ClubHashtag clubHashtag2 = ClubHashtag.createClubHashtag(club2, hashtag);
        when(hashtagRepository.findByName("testHash")).thenReturn(Optional.of(hashtag));
        when(clubHashtagRepository.findByHashtag(hashtag)).thenReturn(List.of(clubHashtag1, clubHashtag2));
        // when
        List<Club> findClubs = service.findClubsWithHashtag(hashtag.getName());
        // then
        assertThat(findClubs.stream().count()).isEqualTo(2);
        assertThat(findClubs.get(0)).isEqualTo(club);
        assertThat(findClubs.get(1)).isEqualTo(club2);
    }
}