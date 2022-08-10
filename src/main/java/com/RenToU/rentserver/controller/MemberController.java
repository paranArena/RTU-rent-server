package com.RenToU.rentserver.controller;


import com.RenToU.rentserver.DTO.MemberDTO;
import com.RenToU.rentserver.application.LoginService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final LoginService loginService;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberDTO> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(loginService.getMyUserWithAuthorities());
    }
}
