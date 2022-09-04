package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.exceptions.CannotRentException;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.exceptions.clubMember.ClubMemberNotFoundException;
import com.RenToU.rentserver.infrastructure.ClubMemberRepository;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.HashtagRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("ClubService 클래스")
@Transactional
class ClubServiceImplWebTest {
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
    ClubMemberRepository clubMemberRepository;
    @Autowired
    ProductRepository productRepository;

    private static String INITIAL_CLUB_NAME = "NEW CLUB";
    private static String INITIAL_CLUB_INTRO = "새로 생성한 클럽입니다.";
    private static String INITIAL_CLUB_THUMB = "www.google.com";
    private static List<String> INITIAL_CLUB_HASHTAGS = List.of("hashtag1","hashtag2");
    private Member owner;
    private Long memberId;
    private Long ownerId;
    private Long clubId;
    private Long secondClubId;


    @BeforeEach
    void setUp() {
        service = new ClubServiceImpl(mapper, clubRepository, memberRepository, hashtagRepository, clubMemberRepository);
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
    @Nested
    @DisplayName("requestJoin메소드는")
    class describe_request_join {
        @Nested
        @DisplayName("가입 신청을 요청하면")
        class data_given {
            @Test
            @DisplayName("ClubMember를 Wait으로 생성한다.")
            void it_makes_club_member_with_waiting() {
                Club club = service.createClub(owner.getId(),INITIAL_CLUB_NAME,INITIAL_CLUB_INTRO,INITIAL_CLUB_THUMB,INITIAL_CLUB_HASHTAGS);
                Member requester = memberRepository.findById(2L).get();
                service.requestClubJoin(club.getId(),requester.getId());
                assertThat(club.getMemberList().get(0).getMember()).isEqualTo(owner);
                assertThat(club.getMemberList().size()).isEqualTo(2);
                assertThat(club.getMemberList().get(1).getMember()).isEqualTo(requester);
                assertThat(club.getMemberList().get(1).getRole()).isEqualTo(ClubRole.WAIT);

            }
        }
    }
    @Nested
    @DisplayName("getMyClubRequests메소드는")
    class Describe_getMyClubRequests {
        @Nested
        @DisplayName("memberId가 주어졌을 때")
        class memberId_given {
            @BeforeEach
            void setup() {
                memberId = 6L;
                clubId = 1L;
                secondClubId = 2L;
            }
            @Test
            @DisplayName(" 가입 대기 상태인 클럽의 리스트를 리턴한다..")
            void it_return_list_of_waiting_clubs() {
                service.requestClubJoin(clubId,memberId);
                service.requestClubJoin(secondClubId,memberId);
                List<Club> clubs = service.getMyClubRequests(memberId);
                assertThat(clubs.size()).isEqualTo(2);
                assertThat(clubs.get(0).getId()).isEqualTo(clubId);
                assertThat(clubs.get(1).getId()).isEqualTo(secondClubId);
            }
        }
    }
    @Nested
    @DisplayName("grantAdmin메소드는")
    class Describe_grantAdmin {
        @Nested
        @DisplayName("memberId가 주어졌을 때")
        class memberId_given {
            @BeforeEach
            void setup() {
                ownerId = 2L;
                memberId = 5L;
                clubId = 1L;

            }
            @Test
            @DisplayName("member의 ClubRole을 Admin으로 바꾼다.")
            void it_change_club_role_to_admin() {
                service.grantAdmin(clubId,ownerId,memberId);
                Member member = memberRepository.findById(memberId).get();
                assertThat(member.getClubList().get(0).getRole()).isEqualTo(ClubRole.ADMIN);
            }
        }
    }
    @Nested
    @DisplayName("leaveClub메소드는")
    class Describe_leaveClub {
        @Nested
        @DisplayName("memberId가 주어졌을 때")
        class memberId_given {
            @BeforeEach
            void setup() {
                memberId = 5L;
                clubId = 1L;

            }
            @Test
            @DisplayName("Clubmember를 삭제한다.")
            void it_delete_club_member() {
                service.leaveClub(clubId,memberId);
                Member member = memberRepository.findById(memberId).get();
                Club club = clubRepository.findById(clubId).get();
                assertThat(member.getClubList()).isEmpty();
                assertThatThrownBy(()->club.findClubMemberByMember(member))
                        .isInstanceOf(ClubMemberNotFoundException.class);
            }
        }
    }

}