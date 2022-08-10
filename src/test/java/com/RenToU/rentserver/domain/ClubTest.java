package com.RenToU.rentserver.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ClubTest {
    private static final String INITIAL_CLUB_NAME = "TestClubName";
    private static final String INITIAL_CLUB_INTRO = "TestClubIntro";
    private static final String INITIAL_CLUB_ThUMBNAILPATH = "TestThumbnailPath";
    
    private ClubMember clubMember;

    @BeforeEach
    void setup(){

    }
    @Test
    void create(){
        Member member = createMember("TestMember", "TestMember@ajou.ac.kr");
        Club club = Club.createClub(INITIAL_CLUB_NAME, INITIAL_CLUB_INTRO, INITIAL_CLUB_ThUMBNAILPATH, member);
        assertThat(club.getName()).isEqualTo(INITIAL_CLUB_NAME);
        assertThat(club.getIntroduction()).isEqualTo(INITIAL_CLUB_INTRO);
        assertThat(club.getMemberList().get(0).getClub().getName()).isEqualTo(club.getName());
    }
    private Member createMember(String name, String email) {
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        return member;
    }
}