package com.RenToU.rentserver.dto.response.preview;

import com.RenToU.rentserver.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreviewDto {

    private Long id;

    private Long clubId;

    private String title;

    private String imagePath;

    private Boolean isPublic;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static NotificationPreviewDto from(Notification notification) {
        if(notification == null) return null;

        return NotificationPreviewDto.builder()
        .id(notification.getId())
        .clubId(notification.getClub().getId())
        .title(notification.getTitle())
        .isPublic(notification.getIsPublic())
        .imagePath(notification.getImagePath())
        .createdAt(notification.getCreatedAt())
        .updatedAt(notification.getUpdatedAt())
        .build();
    }
}
