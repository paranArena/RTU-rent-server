package com.RenToU.rentserver.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.RenToU.rentserver.domain.ClubMember;
import com.github.dozermapper.core.Mapping;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubMemberDto {
    
    private Long id;

    @NotBlank
    @Email
    @Mapping("email")
    @Size(min = 1, max = 50)
    private String email;

    @NotBlank
    @Mapping("name")
    @Size(min = 1, max = 20)
    private String name;

    @NotBlank
    @Mapping("phoneNumber")
    @Size(min = 1, max = 20)
    private String phoneNumber;

    @NotBlank
    @Mapping("studentId")
    @Size(min = 1, max = 20)
    private String studentId;

    @NotBlank
    @Mapping("major")
    private String major;

    public static ClubMemberDto from(ClubMember clubMember) {
        if(clubMember == null) return null;

        return ClubMemberDto.builder()
            .id(clubMember.getId())
            .email(clubMember.getMember().getEmail())
            .name(clubMember.getMember().getName())
            .phoneNumber(clubMember.getMember().getPhoneNumber())
            .studentId(clubMember.getMember().getStudentId())
            .major(clubMember.getMember().getMajor())
            .build();
    }
}

