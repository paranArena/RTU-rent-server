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

    private String title;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static NotificationPreviewDto from(Notification notification) {
        if(notification == null) return null;

        return NotificationPreviewDto.builder()
        .id(notification.getId())
        .title(notification.getTitle())
        .createdAt(notification.getCreatedAt())
        .updatedAt(notification.getUpdatedAt())
        .build();
    }
}
