package com.RenToU.rentserver.controller.member;


import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.EmailDto;
import com.RenToU.rentserver.dto.request.EmailVerifyDto;
import com.RenToU.rentserver.dto.response.MemberInfoDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.github.dozermapper.core.Mapper;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.domain.Member;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final Mapper mapper;

    @GetMapping("/{email}/info")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?>getMemberInfo(@PathVariable("email") String email) {
        Member member = memberService.getUserWithAuthorities(email);
        MemberInfoDto resData = mapper.map(member, MemberInfoDto.class);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.OK, resData));
    }
    @PostMapping("/email/requestCode")
    public ResponseEntity<Void> authEmail(@RequestBody @Valid EmailDto request) {
        memberService.authEmail(request);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/email/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody @Valid EmailVerifyDto request) {
        memberService.verifyCode(request);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.EMAIL_VERIFIED,null));
    }
}
