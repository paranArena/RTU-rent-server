package com.RenToU.rentserver.DTO;

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
public class NotificationDTO {

    private Long id;

    @NotBlank
    @Mapping("title")
    private String title;
    
    @NotBlank
    @Mapping("content")
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    //TODO image
    public static NotificationDTO from(Notification notification){
        if(notification == null) return null;

        return null;
    }
}
