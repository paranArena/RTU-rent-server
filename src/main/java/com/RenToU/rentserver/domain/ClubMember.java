package com.RenToU.rentserver.domain;

import com.RenToU.rentserver.exceptions.NoOwnerPermissionException;
import com.RenToU.rentserver.exceptions.NotClubRoleWaitingException;
import com.RenToU.rentserver.exceptions.NotWaitingException;
import com.RenToU.rentserver.exceptions.club.CannotGrantException;
import com.RenToU.rentserver.exceptions.club.CannotJoinClubException;
import com.RenToU.rentserver.exceptions.NoAdminPermissionException;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "club_id")
    private Club club;

    @Enumerated(EnumType.STRING)
    private ClubRole role;

    public static ClubMember createClubMember(Club club, Member member, ClubRole role) {
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

    public void acceptJoin() {
        if (this.role == ClubRole.WAIT) {
            this.role = ClubRole.USER;
        } else {
            throw new NotClubRoleWaitingException("사용자가 대기 상태가 아닙니다.");
        }
    }

    public void grantAdmin() {
        if (this.role == ClubRole.USER) {
            this.role = ClubRole.ADMIN;
        } else {
            throw new CannotGrantException(this.club.getId(), this.club.getName(), "사용자가 클럽 유저가 아닙니다.");
        }
    }

    public void grantUser() {
        if (this.role == ClubRole.ADMIN) {
            this.role = ClubRole.USER;
        } else {
            throw new CannotGrantException(this.club.getId(), this.club.getName(), "사용자가 클럽 관리자가 아닙니다.");
        }
    }

    public void validateAdmin() {
        if (this.role != ClubRole.ADMIN && this.role != ClubRole.OWNER) {
            throw new NoAdminPermissionException(this.club.getId());
        }
    }

    public boolean isAdmin() {
        if (this.role != ClubRole.ADMIN && this.role != ClubRole.OWNER) {
            return true;
        }
        return false;
    }

    public String toString() {
        return this.getClub().getId() + " " + this.getMember().getId() + " " + this.getRole().toString();
    }

    public void validateOwner() {
        if (this.role != ClubRole.OWNER) {
            throw new NoOwnerPermissionException(this.club.getId());
        }
    }

    public void delete() {
        member.deleteClub(this);
        club.deleteMember(this);
    }

    public void validateWait() {
        if (this.role != ClubRole.WAIT) {
            throw new NotClubRoleWaitingException("사용자가 클럽 가입 대기 상태가 아닙니다.");
        }
    }

    public boolean isUser() {
        if (this.role != ClubRole.USER) {
            return true;
        }
        return false;
    }

    public void deleteMember() {

    }
}
