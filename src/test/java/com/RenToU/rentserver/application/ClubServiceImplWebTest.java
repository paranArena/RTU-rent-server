package com.RenToU.rentserver.application;

import com.RenToU.rentserver.DTO.ClubDTO;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
class ClubServiceImplWebTest {


    @PersistenceContext
    EntityManager em;

    private Mapper mapper;
    @Autowired
    private ClubServiceImpl clubService;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MemberRepository memberRepository;
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
    }


    @Test
    public void createClub(){
        Member member = createMember(INITIAL_MEMBER_NAME,INITIAL_MEMBER_EMAIL);
        ClubDTO clubDto = clubService.createClub(member.getId(),INITIAL_CLUB_NAME,INITIAL_CLUB_INTRO,INITIAL_CLUB_THUMBNAILPATH);
        assertThat(clubDto.getName()).isEqualTo(INITIAL_CLUB_NAME);
        assertThat(clubDto.getMemberList().get(0).getMember().getName()).isEqualTo(member.getName());
    }
    @Test
    public void 가입신청(){
        Member member = createMember(INITIAL_MEMBER_NAME,INITIAL_MEMBER_EMAIL);
        ClubDTO clubDto = clubService.createClub(member.getId(),INITIAL_CLUB_NAME,INITIAL_CLUB_INTRO,INITIAL_CLUB_THUMBNAILPATH);
        Member joiner = createMember("Joiner","Joiner@ajou.ac.kr");
        assertThatThrownBy(()->clubDto.findClubMemberByMember(joiner))
                .isInstanceOf(MemberNotFoundException.class);
        clubService.requestClubJoin(clubDto.getId(),joiner.getId());
        assertThat(clubDto.findClubMemberByMember(joiner).getRole()).isEqualTo(ClubRole.WAIT);
    }
    @Test
    public void 가입승인(){
        Member member = createMember(INITIAL_MEMBER_NAME,INITIAL_MEMBER_EMAIL);
        ClubDTO clubDto = clubService.createClub(member.getId(),INITIAL_CLUB_NAME,INITIAL_CLUB_INTRO,INITIAL_CLUB_THUMBNAILPATH);
        Member joiner = createMember("Joiner","Joiner@ajou.ac.kr");
        clubService.requestClubJoin(clubDto.getId(),joiner.getId());
        assertThat(clubDto.findClubMemberByMember(joiner).getRole()).isEqualTo(ClubRole.WAIT);
        clubService.acceptClubJoin(clubDto.getId(),member.getId(), joiner.getId());
        assertThat(clubDto.findClubMemberByMember(joiner).getRole()).isEqualTo(ClubRole.USER);
    }
    @Test
    public void 가입승인_예외_승인권한_없음(){
        Member owner = createMember(INITIAL_MEMBER_NAME,INITIAL_MEMBER_EMAIL);
        ClubDTO clubDto = clubService.createClub(owner.getId(),INITIAL_CLUB_NAME,INITIAL_CLUB_INTRO,INITIAL_CLUB_THUMBNAILPATH);
        Member member = createMember(INITIAL_MEMBER_NAME,INITIAL_MEMBER_EMAIL);
        Member joiner = createMember("Joiner","Joiner@ajou.ac.kr");
        clubService.requestClubJoin(clubDto.getId(),joiner.getId());
        assertThatThrownBy(()->clubService.acceptClubJoin(clubDto.getId(),member.getId(), joiner.getId()))
                .isInstanceOf(MemberNotFoundException.class);
    }
    @Test
    public void 가입승인_예외_신청하지_않은_사용자(){
        Member owner = createMember(INITIAL_MEMBER_NAME,INITIAL_MEMBER_EMAIL);
        ClubDTO clubDto = clubService.createClub(owner.getId(),INITIAL_CLUB_NAME,INITIAL_CLUB_INTRO,INITIAL_CLUB_THUMBNAILPATH);
        Member joiner = createMember("Joiner","Joiner@ajou.ac.kr");
        assertThatThrownBy(()->clubService.acceptClubJoin(clubDto.getId(),owner.getId(), joiner.getId()))
                .isInstanceOf(MemberNotFoundException.class);
    }
    private Member createMember(String name, String email) {
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        em.persist(member);
        return member;
    }
}