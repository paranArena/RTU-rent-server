package com.RenToU.rentserver.DTO.service;

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
public class NotificationServiceDto {

    // other request var
    private long memberId;
    private long clubId;
    
    // mapped by CreateNotifiationDto 
    private String title;
    private String content;
}
