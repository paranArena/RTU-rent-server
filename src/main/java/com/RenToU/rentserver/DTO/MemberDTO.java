package com.RenToU.rentserver.DTO;

import com.RenToU.rentserver.domain.Authority;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
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
    @Mapping("email")
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Mapping("password")
    private String password;
    @NotBlank
    @Mapping("name")
    private String name;
    @NotBlank
    @Mapping("phoneNumber")
    private String phoneNumber;
    @NotBlank
    @Mapping("studentId")
    private String studentId;
    @NotBlank
    @Mapping("major")
    private String major;

    private Set<Authority> authorities;

    @OneToMany(mappedBy = "member")
    private List<ClubMember> clubList = new ArrayList<>();

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
//                .authorityDtoSet(member.getAuthorities().stream()
//                        .map(authority -> AuthorityDTO.builder()
//                                .authorityName(authority.getAuthority().getAuthorityName())
//                                .build()
//                        )
//                        .collect(Collectors.toSet()))
                .build();
    }

}
