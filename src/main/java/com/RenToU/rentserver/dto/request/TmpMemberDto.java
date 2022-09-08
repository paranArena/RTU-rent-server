package com.RenToU.rentserver.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TmpMemberDto {

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 10, message = "이름을 2자 이상 10자 이하로 입력해주세요.")
    private String name;
    @NotBlank(message = "학번을 입력헤주세요.")
    @Size(min = 6, max = 11, message = "학번을 6자 이상 11자 이하로 입력해주세요.")
    private String studentId;
}