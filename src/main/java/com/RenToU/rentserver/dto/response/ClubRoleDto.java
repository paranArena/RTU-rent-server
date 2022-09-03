package com.RenToU.rentserver.dto.response;

import com.RenToU.rentserver.domain.ClubRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubRoleDto {
    private ClubRole clubRole;
}
