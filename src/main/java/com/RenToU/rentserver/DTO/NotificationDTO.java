package com.RenToU.rentserver.DTO;

import com.github.dozermapper.core.Mapping;

import javax.validation.constraints.NotBlank;

public class NotificationDTO {
    private Long id;
    @NotBlank
    @Mapping("title")
    private String title;
    @NotBlank
    @Mapping("content")
    private String content;
}
