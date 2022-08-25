package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.HashtagRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@DisplayName("ClubService 클래스")
@Transactional
class ClubServiceImplTest {
    private ClubService service;
    @Autowired
    private Mapper mapper;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    HashtagRepository hashtagRepository;
    @Autowired
    ClubRepository clubRepository;
    @Autowired
    ProductRepository productRepository;

    private static String INITIAL_CLUB_NAME = "NEW CLUB";
    private static String INITIAL_CLUB_INTRO = "새로 생성한 클럽입니다.";
    private static String INITIAL_CLUB_THUMB = "www.google.com";
    private static List<String> INITIAL_CLUB_HASHTAGS = List.of("hashtag1","hashtag2");
    private Member owner;


    @BeforeEach
    void setUp() {
        service = new ClubServiceImpl(mapper, clubRepository, memberRepository, hashtagRepository, productRepository);
        owner = memberRepository.findById(1L).get();
    }

    @Nested
    @DisplayName("createClub메소드는")
    class Describe_createClub {
        @Nested
        @DisplayName("멤버ID,클럽이름,클럽 설명, 썸네일 주소, 해쉬태그목록이 주어진다면")
        class data_given {
            @Test
            @DisplayName("새 클럽을 생성하고 리턴한다.")
            void it_return_new_club() {
                Club club = service.createClub(owner.getId(),INITIAL_CLUB_NAME,INITIAL_CLUB_INTRO,INITIAL_CLUB_THUMB,INITIAL_CLUB_HASHTAGS);
                assertThat(club.getMemberList().get(0).getMember().getId()).isEqualTo(owner.getId());
                assertThat(club.getName()).isEqualTo(INITIAL_CLUB_NAME);
                assertThat(club.getIntroduction()).isEqualTo(INITIAL_CLUB_INTRO);
                assertThat(club.getThumbnailPath()).isEqualTo(INITIAL_CLUB_THUMB);
                assertThat(club.getHashtagNames()).isEqualTo(INITIAL_CLUB_HASHTAGS);
            }
        }
    }

}