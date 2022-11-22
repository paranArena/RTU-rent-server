package com.RenToU.rentserver.dto.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.github.dozermapper.core.Mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class V1CreateNotificationDto {

    @NotBlank
    @Mapping("title")
    private String title;

    @NotBlank
    @Mapping("content")
    private String content;

    @Mapping("imagePaths")
    private List<String> imagePaths;
}
