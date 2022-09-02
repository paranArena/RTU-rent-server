package com.RenToU.rentserver.dto.request;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

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

    private List<MultipartFile> image;
}

