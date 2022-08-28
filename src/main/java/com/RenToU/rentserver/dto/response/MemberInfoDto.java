package com.RenToU.rentserver.dto.response;

import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.dto.RentalDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDto {

    private Long id;

    private String email;

    private String name;

    private String phoneNumber;

    private String studentId;

    private String major;

    private boolean activated;

    // private List<MemberClubDto> clubList;

    // private List<RentalDto> rentals;

    private Set<AuthorityDto> authorities;

    // public static MemberDto from(Member member) {
    //     if(member == null) return null;

    //     return MemberDto.builder()
    //         .id(member.getId())
    //         .email(member.getEmail())
    //         .name(member.getName())
    //         .phoneNumber(member.getPhoneNumber())
    //         .studentId(member.getStudentId())
    //         .major(member.getMajor())
    //         .activated(member.isActivated())
    //         .clubList(member.getClubList().stream()
    //             .map(club -> MemberClubDto.from(club))
    //             .collect(Collectors.toList()))
    //         .rentals(member.getRentals().stream()
    //             .map(rental -> RentalDto.from(rental))
    //             .collect(Collectors.toList()))
    //         .authorities(member.getAuthorities().stream()
    //             .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
    //             .collect(Collectors.toSet()))
    //         .build();
    // }
}