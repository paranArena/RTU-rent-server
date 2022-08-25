package com.RenToU.rentserver.controller.member;


import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.dto.response.MemberDto;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberDto> getMyInfo(HttpServletRequest request) {
        Member member = memberService.getMyUserWithAuthorities();
        return ResponseEntity.ok(MemberDto.from(member));
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MemberDto> getMemberInfo(@PathVariable("email") String email) {
        Member member = memberService.getUserWithAuthorities(email);
        return ResponseEntity.ok(MemberDto.from(member));
    }

    @GetMapping("/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable("email") String email) {

        return ResponseEntity.ok(memberService.checkEmailDuplicate(email));
    }
}
