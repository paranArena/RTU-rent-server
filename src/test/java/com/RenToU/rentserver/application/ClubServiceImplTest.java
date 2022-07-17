//package com.RenToU.rentserver.application;
//
//import com.RenToU.rentserver.domain.Club;
//import com.RenToU.rentserver.domain.Member;
//import com.RenToU.rentserver.infrastructure.ClubRepository;
//import com.RenToU.rentserver.infrastructure.JPAClubRepository;
//import com.RenToU.rentserver.infrastructure.JPAMemberRepository;
//import com.RenToU.rentserver.infrastructure.MemberRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//
//class ClubServiceImplTest {
//
//    private ClubServiceImpl clubService;
//    private JPAClubRepository clubRepository = mock(JPAClubRepository.class);
//    private JPAMemberRepository memberRepository = mock(JPAMemberRepository.class);
//    private static final Long INITIAL_MEMBER_ID = 1L;
//    private static final String INITIAL_MEMBER_NAME = "TestMemberName";
//    private static final String INITIAL_MEMBER_EMAIL = "testemail@ajou.ac.kr";
//
//    private static final Long INITIAL_CLUB_ID = 1L;
//    private static final String INITIAL_CLUB_NAME = "TestClubName";
//    private static final String INITIAL_CLUB_INTRO = "TetClubIntrodution.";
//
//    @BeforeEach
//    void setup(){
//        clubService = new ClubServiceImpl(clubRepository,memberRepository);
//        Member member = Member.builder()
//                .id(INITIAL_MEMBER_ID)
//                .name(INITIAL_MEMBER_NAME)
//                .email(INITIAL_MEMBER_EMAIL)
//                .build();
//        given(memberRepository.findById(INITIAL_MEMBER_ID)).willReturn(member);
//        given(clubRepository.save(any(Club.class))).will(invocation ->{
//            Club source = invocation.getArgument(0);
//                return Club.builder()
//                        .id(INITIAL_CLUB_ID)
//                        .name(source.getName())
//                        .introduction(source.getIntroduction())
//                        .memberList(source.getMemberList())
//                        .build();
//        });
//    }
//
//    @Test
//    public void createClub(){
//        Long clubId = clubService.createClub(INITIAL_MEMBER_ID,INITIAL_CLUB_NAME,INITIAL_CLUB_INTRO);
//        verify(memberRepository).findById(INITIAL_MEMBER_ID);
//        verify(clubRepository).save(any(Club.class));
//        assertThat(clubId).isEqualTo(INITIAL_CLUB_ID);
//    }
//}