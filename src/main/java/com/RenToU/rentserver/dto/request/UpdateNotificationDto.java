package com.RenToU.rentserver.dto.request;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNotificationDto {

    @NotBlank
    @Mapping("title")
    private String title;
    
    @NotBlank
    @Mapping("content")
    private String content;

    @NotBlank
    @Mapping("isPublic")
    private String isPublic;
    @Mapping("notification_id")
    private Long notificationId;

    private List<MultipartFile> image;

    private List<String> imagePath;
}

