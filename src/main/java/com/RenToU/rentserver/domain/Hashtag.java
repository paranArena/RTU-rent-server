package com.RenToU.rentserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hashtag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "hashtag", fetch = LAZY, cascade = CascadeType.ALL)
    private List<ClubHashtag> clubs = new ArrayList<>();

    public static Hashtag createHashtag(String hashtagName) {
        return Hashtag.builder()
                .name(hashtagName)
                .build();
    }

    public void addClubs(ClubHashtag clubHashtag) {
        this.clubs.add(clubHashtag);
        clubHashtag.setHashtag(this);
    }

}
