package com.RenToU.rentserver.domain;

import com.RenToU.rentserver.exceptions.club.CannotJoinClubException;
import com.RenToU.rentserver.exceptions.NoAdminPermissionException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubMember extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Enumerated(EnumType.STRING)
    private ClubRole role;

    public static ClubMember createClubMember(Club club, Member member,ClubRole role) {
        ClubMember clubMember = ClubMember.builder()
                .role(role)
                .build();
        club.addClubMember(clubMember);
        member.addClubList(clubMember);
        return clubMember;
    }

    public void setClub(Club club) {
        this.club = club;
    }
    public void setMember(Member member) {
        this.member = member;
    }
    public void acceptJoin(){
        if(this.role == ClubRole.WAIT) {
            this.role = ClubRole.USER;
        }else{
            throw new CannotJoinClubException(this.club.getId(),this.club.getName(),"사용자가 대기 상태가 아닙니다.");
        }
    }

    public void validateAdmin() {
        if(this.role != ClubRole.ADMIN && this.role != ClubRole.OWNER){
            throw new NoAdminPermissionException(this.club.getId());
        }
    }
    public String toString(){
        return this.getClub().getId() + " " + this.getMember().getId() + " "  + this.getRole().toString();
    }
}
