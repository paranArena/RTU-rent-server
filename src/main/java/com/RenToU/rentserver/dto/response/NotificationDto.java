package com.RenToU.rentserver.dto.response;

import com.RenToU.rentserver.domain.Notification;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private Long id;

    @NotBlank
    @Mapping("title")
    private String title;

    @NotBlank
    @Mapping("content")
    private String content;

    private String imagePath;

    private Boolean isPublic;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static NotificationDto from(Notification notification) {
        if (notification == null)
            return null;

        return NotificationDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .imagePath(notification.getImagePath())
                .isPublic(notification.getIsPublic())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }
}
