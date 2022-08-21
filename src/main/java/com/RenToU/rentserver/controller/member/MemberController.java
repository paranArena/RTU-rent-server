package com.RenToU.rentserver.controller.member;


import com.RenToU.rentserver.DTO.MemberDTO;
import com.RenToU.rentserver.application.MemberService;

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
    public ResponseEntity<MemberDTO> getMyInfo(HttpServletRequest request) {
        return ResponseEntity.ok(memberService.getMyUserWithAuthorities());
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MemberDTO> getMemberInfo(@PathVariable("email") String email) {
        return ResponseEntity.ok(memberService.getUserWithAuthorities(email));
    }

    @GetMapping("/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable("email") String email) {

        return ResponseEntity.ok(memberService.checkEmailDuplicate(email));
    }
}
