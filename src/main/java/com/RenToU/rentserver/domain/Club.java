package com.RenToU.rentserver.domain;

import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;
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
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Club extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    private String introduction;

    private String thumbnailPath;

    @Builder.Default
    @OneToMany(mappedBy = "club", fetch = LAZY, cascade = CascadeType.ALL)
    private List<ClubMember> memberList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "club", fetch = LAZY, cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "club", fetch = LAZY, cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "club", fetch = LAZY, cascade = CascadeType.ALL)
    private List<ClubHashtag> hashtags = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "club", fetch = LAZY, cascade = CascadeType.ALL)
    private List<Coupon> coupons = new ArrayList<>();

    // Setter
    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    // 연관관계 편의 메소드
    public void addClubMember(ClubMember clubMember) {
        memberList.add(clubMember);
        clubMember.setClub(this);
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setClub(this);
    }

    public void addHashtag(ClubHashtag clubHashtag) {
        hashtags.add(clubHashtag);
        clubHashtag.setClub(this);
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
        notification.setClub(this);
    }

    public static Club createClub(String clubName, String clubIntro, String thumbnailPath, Member member,
            List<Hashtag> hashtags) {
        Club club = Club.builder()
                .name(clubName)
                .introduction(clubIntro)
                .thumbnailPath(thumbnailPath)
                .build();
        for (Hashtag hashtag : hashtags) {
            ClubHashtag.createClubHashtag(club, hashtag);
        }
        ClubMember.createClubMember(club, member, ClubRole.OWNER);
        return club;
    }

    public ClubMember findClubMemberByMemberId(Long memberId) {
        ClubMember clubMember = this.getMemberList().stream().filter(cm -> {
            return cm.getMember().getId().equals(memberId);
        }).findFirst().orElseThrow(() -> new CustomException(ClubErrorCode.CLUBMEMBER_NOT_FOUND_BY_MEMBERID));

        return clubMember;
    }

    public List<String> getHashtagNames() {
        return this.hashtags.stream().map(hashtag -> {
            return hashtag.getHashtag().getName();
        }).collect(Collectors.toList());
    }

    public void deleteMember(ClubMember clubMember) {
        this.memberList.remove(clubMember);
    }

    public void deleteProduct(Product product) {
        this.products.remove(product);

    }

    public void updateClub(String name, String intro, String thumbnailPath, List<Hashtag> clubHashtags) {
        this.name = name;
        this.introduction = intro;
        this.thumbnailPath = thumbnailPath;
        this.hashtags = new ArrayList<>();
        for (Hashtag hashtag : clubHashtags) {
            ClubHashtag.createClubHashtag(this, hashtag);
        }
    }

    public void deleteHashtag(ClubHashtag hashtag) {
        this.getHashtags().remove(hashtag);
        hashtag.setClub(null);
    }

    public void addCoupon(Coupon coupon) {
        coupons.add(coupon);
        coupon.setClub(this);
    }
}
