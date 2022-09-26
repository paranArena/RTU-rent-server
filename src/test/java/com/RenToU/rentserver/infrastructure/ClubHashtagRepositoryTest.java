package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubHashtag;
import com.RenToU.rentserver.domain.Hashtag;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.infrastructure.jpa.ClubHashtagRepository;
import com.RenToU.rentserver.infrastructure.jpa.MemberRepository;
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
class ClubHashtagRepositoryTest {
    @Autowired
    private ClubHashtagRepository clubHashtagRepository;
    @Autowired
    private MemberRepository memberRepository;
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
    @DisplayName("해쉬태그로 그룹이 여러개일때 조회가 잘 되는지 확인")
    void findClubsByHashtagName() {
        // given

        clubHashtagRepository.save(ClubHashtag.createClubHashtag(club1, hashtag1));
        clubHashtagRepository.save(ClubHashtag.createClubHashtag(club2, hashtag1));
        // when
        List<ClubHashtag> findClubs = clubHashtagRepository.findByHashtag(hashtag1);
        // then
        assertThat(findClubs.stream().count()).isEqualTo(2);
        assertThat(findClubs.get(0).getClub()).isEqualTo(club1);
        assertThat(findClubs.get(0).getHashtag()).isEqualTo(hashtag1);
    }

}