package com.RenToU.rentserver.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {

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

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ClubMember> clubList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Rental> rentals = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "member_authority", joinColumns = {
            @JoinColumn(name = "member_id", referencedColumnName = "member_id") }, inverseJoinColumns = {
                    @JoinColumn(name = "authority_name", referencedColumnName = "authority_name") })
    private Set<Authority> authorities;

    public static Member createTempMember(String studentName, String studentId,Club club) {
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        Member member = Member.builder()
                .name(studentName)
                .studentId(studentId)
                .email(null)
                .password(null)
                .clubList(new ArrayList<>())
                .rentals(new ArrayList<>())
                .major(null)
                .phoneNumber(null)
                .activated(false)
                .build();
        member.setAuthorities(Collections.singleton(authority));
        return member;
    }

    /**
     * 연관관계 편의 메소드
     */
    public void addClubList(ClubMember clubMember) {
        this.clubList.add(clubMember);
        clubMember.setMember(this);
    }

    public static Member createMember(String name, String email) {
        Member member = Member.builder()
                .name(name)
                .email(email)
                .build();
        return member;
    }

    public static Member createMemberWithId(Long id, String name, String email) {
        Member member = Member.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
        return member;
    }

    public void addRental(Rental rental) {
        this.rentals.add(rental);
        rental.setMember(this);
    }

    public List<ClubMember> getClubListWithoutWait() {
        return this.clubList.stream().filter(cm -> cm.getRole() != ClubRole.WAIT).collect(Collectors.toList());
    }

    public void deleteClub(ClubMember clubMember) {
        this.clubList.remove(clubMember);
    }

    public void deleteRental(Rental rental) {
        rentals.remove(rental);
    }

}
