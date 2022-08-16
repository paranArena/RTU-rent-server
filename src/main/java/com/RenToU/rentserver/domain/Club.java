package com.RenToU.rentserver.domain;

import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Club extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "club_id")
    private Long id;

    private String name;

    private String thumbnailPath;

    private String introduction;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubMember> memberList;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Notification> notifications;
    
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Product> products;

    //연관관계 편의 메소드
    public void addClubMember(ClubMember clubMember){
        memberList.add(clubMember);
        clubMember.setClub(this);
    }
    public void addProduct(Product product) {
        products.add(product);
        product.setClub(this);
    }
    public void addNotification(Notification notification){
        notifications.add(notification);
        notification.setClub(this);
    }


    public static Club createClub(String clubName, String clubIntro, String thumbnailPath,Member member) {
        Club club = Club.builder()
                .name(clubName)
                .introduction(clubIntro)
                .thumbnailPath(thumbnailPath)
                .build();
        club.memberList = new ArrayList<>();
        club.notifications = new ArrayList<>();
        ClubMember.createClubMember(club, member, ClubRole.OWNER);
        return club;
    }

    public ClubMember findClubMemberByMember(Member member){
        Optional<ClubMember> clubMember = this.getMemberList().stream().filter(cm -> {
            return cm.getMember().getId() == member.getId();
        }).findFirst();
        if(clubMember.isEmpty()){
            throw new MemberNotFoundException(member.getId());
        }
        return clubMember.get();
    }


}
