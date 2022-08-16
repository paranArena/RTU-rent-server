package com.RenToU.rentserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubDTO {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String introduction;

    private String thumbnailPath;
}

