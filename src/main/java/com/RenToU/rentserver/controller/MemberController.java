package com.RenToU.rentserver.controller;


import com.RenToU.rentserver.DTO.MemberDTO;
import com.RenToU.rentserver.application.LoginService;
import com.RenToU.rentserver.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final LoginService loginService;

    MemberController(LoginService loginService){
        this.loginService = loginService;
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberDTO> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(loginService.getMyUserWithAuthorities());
    }
//
//    @GetMapping("/user/{username}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public ResponseEntity<MemberDTO> getUserInfo(@PathVariable String username) {
//        return ResponseEntity.ok(loginService.getUserWithAuthorities(username));
//    }
}
