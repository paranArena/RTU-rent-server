package com.RenToU.rentserver.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClubTest {
    Club club;
    private static final Long INITIAL_ID = 1L;

    private static final String INITIAL_NAME = "test club";
    private static final String INITIAL_INTRO = "this is test club";
    private static final String INITIAL_THUMB = "www.thumbnail.com";
    private static Member INITIAL_OWNER = Member.createMember("admin", "admin@admin.com");
    private static final List<Hashtag> INITIAL_HASHTAG = new ArrayList<>();

    @BeforeEach
    void setup() {
        club = Club.createClub(INITIAL_NAME, INITIAL_INTRO, INITIAL_THUMB, INITIAL_OWNER, INITIAL_HASHTAG);
    }

    @DisplayName("findClubByMember 동작 테스트")
    @Test
    public void findClubMyMemberTest() {
        // given
        // when
//        ClubMember findMember = club.findClubMemberByMember(INITIAL_OWNER);
//        // then
//        assertThat(findMember.getMember()).isEqualTo(INITIAL_OWNER);
    }
}