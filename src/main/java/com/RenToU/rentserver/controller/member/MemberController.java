package com.RenToU.rentserver.controller.member;

import com.RenToU.rentserver.dto.StatusCode;
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

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final Mapper mapper;

    @GetMapping("/{email}/info")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getMemberInfo(@PathVariable("email") String email) {
        Member member = memberService.getUserWithAuthorities(email);
        MemberInfoDto resData = mapper.map(member, MemberInfoDto.class);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.OK, resData));
    }

    @PostMapping("/{memberId}/report")
    public ResponseEntity<?> reportMember(@PathVariable Long memberId) {

        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.REPORT_MEMBER));
    }

}
