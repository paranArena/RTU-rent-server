package com.RenToU.rentserver.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {
        @Email(message = "이메일 형식을 입력해주세요.")
        @NotBlank(message = "이메일을 입력해주세요.")
        @Pattern(regexp = ".+@ajou.ac.kr", message = "아주대학교 이메일을 입력해주세요.")
        private String email;
}