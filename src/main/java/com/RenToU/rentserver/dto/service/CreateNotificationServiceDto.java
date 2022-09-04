package com.RenToU.rentserver.dto.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationServiceDto {

    // other request var
    private long memberId;
    private long clubId;

    // mapped by CreateNotifiationDto
    private String title;
    private String content;
    private List<String> imagePaths;
}
