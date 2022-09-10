package com.RenToU.rentserver.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class EmailVerifyDto {
        @Email
        @NotBlank(message = "이메일(필수)")
        private String email;
        @NotBlank(message = "인증코드(필수)")
        private String code;
}