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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Arrays;

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
            throw new CustomException(ClubErrorCode.NOT_WAIT_USER);
        }
    }

    public void grantAdmin() {
        if (this.role == ClubRole.USER) {
            this.role = ClubRole.ADMIN;
        } else {
            throw new CustomException(ClubErrorCode.CANT_GRANT_ADMIN);
        }
    }

    public void grantUser() {
        if (this.role == ClubRole.ADMIN) {
            this.role = ClubRole.USER;
        } else {
            throw new CustomException(ClubErrorCode.CANT_GRANT_USER);
        }
    }

    public void validateAdmin() {
        if (this.role != ClubRole.ADMIN && this.role != ClubRole.OWNER) {
            throw new CustomException(ClubErrorCode.NO_ADMIN_PERMISSION);
        }
    }

    public void validateRole(ClubRole... clubRoles) {
        if (Arrays.stream(clubRoles).noneMatch(role -> role == this.role)) {
            throw new CustomException(ClubErrorCode.NO_MATCHING_ROLE);
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
            throw new CustomException(ClubErrorCode.NO_ADMIN_PERMISSION);
        }
    }

    public void validateUser() {
        if (this.role != ClubRole.OWNER && this.role != ClubRole.ADMIN && this.role != ClubRole.USER) {
            throw new CustomException(ClubErrorCode.NO_USER_PERMISSION);
        }
    }

    public void delete() {
        member.deleteClub(this);
        club.deleteMember(this);
    }

    public void validateWait() {
        if (this.role != ClubRole.WAIT) {
            throw new CustomException(ClubErrorCode.NOT_WAIT_USER);
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
