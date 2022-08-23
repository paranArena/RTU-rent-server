package com.RenToU.rentserver.DTO;

import com.RenToU.rentserver.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email
    @Size(min = 1, max = 50)
    private String email;

    // @NotBlank(message = "비밀번호를 입력해주세요")
    // @Size(min = 1, max = 100)
    // private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 1, max = 20)
    private String name;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    private String phoneNumber;

    @NotBlank(message = "학번을 입력헤주세요.")
    @Size(min = 1, max = 20)
    private String studentId;

    @NotBlank(message = "전공을 입력해주세요.")
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