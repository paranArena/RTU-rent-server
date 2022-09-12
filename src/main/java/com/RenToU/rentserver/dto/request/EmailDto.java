package com.RenToU.rentserver.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class EmailDto {
        @Email(message = "이메일 형식을 입력해주세요.")
        @NotBlank(message = "이메일을 입력해주세요.")
        @Pattern(regexp = ".+@ajou.ac.kr", message = "아주대학교 이메일을 입력해주세요.")
        private String email;
}