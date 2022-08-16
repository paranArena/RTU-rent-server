package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ClubServiceImplTest {

    private ClubServiceImpl clubService;
    private ClubRepository clubRepository = mock(ClubRepository.class);
    private ProductRepository productRepository = mock(ProductRepository.class);
    private MemberRepository memberRepository = mock(MemberRepository.class);

    private Mapper mapper;
    private static final Long INITIAL_MEMBER_ID = 1L;
    private static final String INITIAL_MEMBER_NAME = "TestMemberName";
    private static final String INITIAL_MEMBER_EMAIL = "testemail@ajou.ac.kr";

    private static final Long INITIAL_CLUB_ID = 1L;
    private static final String INITIAL_CLUB_NAME = "TestClubName";
    private static final String INITIAL_CLUB_INTRO = "TetClubIntrodution.";
    private static final String INITIAL_CLUB_THUMBNAILPATH = "test/thumbnail/path";

    @BeforeEach
    void setup(){
        clubService = new ClubServiceImpl(mapper,clubRepository,memberRepository,productRepository);
        Member member = Member.builder()
                .id(INITIAL_MEMBER_ID)
                .name(INITIAL_MEMBER_NAME)
                .email(INITIAL_MEMBER_EMAIL)
                .build();
        given(memberRepository.findById(INITIAL_MEMBER_ID)).willReturn(Optional.of(member));
        given(clubRepository.save(any(Club.class))).will(invocation ->{
            Club source = invocation.getArgument(0);
                return Club.builder()
                        .id(INITIAL_CLUB_ID)
                        .name(source.getName())
                        .introduction(source.getIntroduction())
                        .memberList(source.getMemberList())
                        .build();
        });
    }

    @Test
    public void createClub(){
        Club club = clubService.createClub(INITIAL_MEMBER_ID,INITIAL_CLUB_NAME,INITIAL_CLUB_INTRO,INITIAL_CLUB_THUMBNAILPATH);
        verify(memberRepository).findById(INITIAL_MEMBER_ID);
        verify(clubRepository).save(any(Club.class));
        assertThat(club.getId()).isEqualTo(INITIAL_CLUB_ID);
    }
}