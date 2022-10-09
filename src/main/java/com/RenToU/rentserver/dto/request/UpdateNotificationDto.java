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
