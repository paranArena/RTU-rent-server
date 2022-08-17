package com.RenToU.rentserver.DTO;

import com.RenToU.rentserver.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long id;

    @NotBlank
    @Email
    @Mapping("email")
    @Size(min = 1, max = 50)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Mapping("password")
    @Size(min = 1, max = 100)
    private String password;

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

    private boolean activated;

    private List<MemberClubDTO> clubList;

    private List<RentalDTO> rentals;

    private Set<AuthorityDTO> authorityDtoSet;

    public static MemberDTO from(Member member) {
        if(member == null) return null;

        return MemberDTO.builder()
            .id(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .phoneNumber(member.getPhoneNumber())
            .studentId(member.getStudentId())
            .major(member.getMajor())
            .activated(member.isActivated())
            .clubList(member.getClubList().stream()
                .map(club -> MemberClubDTO.from(club))
                .collect(Collectors.toList()))
            .rentals(member.getRentals().stream()
                .map(rental -> RentalDTO.from(rental))
                .collect(Collectors.toList()))
            .authorityDtoSet(member.getAuthorities().stream()
                .map(authority -> AuthorityDTO.builder().authorityName(authority.getAuthorityName()).build())
                .collect(Collectors.toSet()))
            .build();
    }
}