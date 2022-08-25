package com.RenToU.rentserver.DTO.request;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateNotificationDto {

    @NotBlank
    @Mapping("title")
    private String title;
    
    @NotBlank
    @Mapping("content")
    private String content;

    //TODO image
}

