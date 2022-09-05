package com.RenToU.rentserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
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

    private Set<AuthorityDto> authorities;
}