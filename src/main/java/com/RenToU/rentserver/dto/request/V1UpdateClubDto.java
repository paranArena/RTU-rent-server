package com.RenToU.rentserver.dto.request;

import com.github.dozermapper.core.Mapping;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class V1UpdateClubDto {

    @NotBlank
    @Mapping("name")
    private String name;

    @NotBlank
    @Mapping("intro")
    private String intro;

    @Mapping("imagePaths")
    private List<String> imagePaths;

    @Mapping("hashtags")
    private List<String> hashtags;
}
