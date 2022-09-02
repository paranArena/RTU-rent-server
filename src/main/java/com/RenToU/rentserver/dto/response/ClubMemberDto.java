package com.RenToU.rentserver.dto.response;

import com.RenToU.rentserver.domain.ClubMember;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubMemberDto {
    
    private Long id;

    private String email;

    private String name;

    private String phoneNumber;

    private String studentId;

    private String major;

    public static ClubMemberDto from(ClubMember clubMember) {
        if(clubMember == null) return null;

        return ClubMemberDto.builder()
            .id(clubMember.getMember().getId())
            .email(clubMember.getMember().getEmail())
            .name(clubMember.getMember().getName())
            .phoneNumber(clubMember.getMember().getPhoneNumber())
            .studentId(clubMember.getMember().getStudentId())
            .major(clubMember.getMember().getMajor())
            .build();
    }
}

