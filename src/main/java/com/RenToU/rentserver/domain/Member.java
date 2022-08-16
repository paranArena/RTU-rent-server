package com.RenToU.rentserver.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity{

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "phoneNumber", length = 20, unique = true)
    private String phoneNumber;

    @Column(name = "studentId", length = 20, unique = true)
    private String studentId;

    @Column(name = "major", length = 50)
    private String major;

    @Column(name = "activated")
    private boolean activated;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ClubMember> clubList;

    // @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    // private List<MemberAuthority> authorities = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Rental> rentals;

   @ManyToMany
   @JoinTable(
           name = "member_authority",
           joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
           inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
   private Set<Authority> authorities;

    /**
     *연관관계 편의 메소드
     */
    // public void addMemberAuth(MemberAuthority memberAuthority) {
    //     this.authorities.add(memberAuthority);
    //     memberAuthority.setMember(this);
    // }
    public void addClubList(ClubMember clubMember) {
        this.clubList.add(clubMember);
        clubMember.setMember(this);
    }
    public Member createMember(String name,String email){
        Member member = Member.builder()
                .name(name)
                .email(email)
                .build();
        member.clubList = new ArrayList<>();
        return member;
    }
}

