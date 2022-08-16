package com.RenToU.rentserver.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;

    private String studentId;

    private String major;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ClubMember> clubList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberAuthority> authorities = new ArrayList<>();

    @Column(name = "activated")
    private boolean activated;

//    @ManyToMany
//    @JoinTable(
//            name = "member_authority",
//            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
//            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
//    private Set<Authority> authorities;

//    @CreatedDate
//    @Column(updatable = false, nullable = false)
//    private LocalDateTime createdAt;

//    @LastModifiedDate
//    private LocalDateTime updatedAt;

    /**
     *연관관계 편의 메소드
     */
    public void addMemberAuth(MemberAuthority memberAuthority) {
        this.authorities.add(memberAuthority);
        memberAuthority.setMember(this);
    }
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

    public void setNewUser() {
        this.activated = true;
        this.authorities = new ArrayList<>();
        this.clubList = new ArrayList<>();
    }

    public List<Authority> getAuths(){
        return authorities.stream().map(authorities ->{
            return authorities.getAuthority();
        }).collect(Collectors.toList());
    }
}

