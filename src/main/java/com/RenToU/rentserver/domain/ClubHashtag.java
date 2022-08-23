package com.RenToU.rentserver.domain;
import com.RenToU.rentserver.domain.BaseTimeEntity;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Hashtag;
import com.RenToU.rentserver.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubHashtag extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "club_hashtag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
    private Club club;
    public void setClub(Club club) {
        this.club = club;
    }
    public void setHashtag(Hashtag hashtag) {
        this.hashtag = hashtag;
    }

    public static void createClubHashtag(Club club, Hashtag hashtag){
        ClubHashtag clubHashtag = new ClubHashtag();
        club.addHashtag(clubHashtag);
        hashtag.addClubs(clubHashtag);
    }
}
