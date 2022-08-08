package com.RenToU.rentserver.domain;

import com.RenToU.rentserver.exceptions.CannotJoinClubException;
import com.RenToU.rentserver.exceptions.NoAdminPermissionException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubMember extends BaseTimeEntity {
    @Id @GeneratedValue
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

    // @CreatedDate
    // @Column(updatable = false, nullable = false)
    // private LocalDateTime createdAt;

    // @LastModifiedDate
    // private LocalDateTime updatedAt;

    public static ClubMember createClubMember(Member member,ClubRole role) {
        ClubMember clubMember = ClubMember.builder()
                .member(member)
                .role(role)
                .build();
        return clubMember;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public void acceptJoin(){
        if(this.role == ClubRole.WAIT) {
            this.role = ClubRole.USER;
        }else{
            throw new CannotJoinClubException(this.club.getId(),this.club.getName());
        }
    }

    public void validateAdmin() {
        if(this.role != ClubRole.ADMIN && this.role != ClubRole.OWNER){
            throw new NoAdminPermissionException(this.club.getId());
        }
    }
}
