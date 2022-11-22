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
public class V1UpdateNotificationDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Mapping("title")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Mapping("content")
    private String content;

    @NotBlank(message = "공개 여부를 선택해주세요.")
    @Mapping("isPublic")
    private String isPublic;

    private List<String> imagePaths;
}
