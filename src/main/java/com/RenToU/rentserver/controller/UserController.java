package com.RenToU.rentserver.controller;


import com.RenToU.rentserver.DTO.MemberDTO;
import com.RenToU.rentserver.application.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UserController {
    private final LoginService loginService;

    public UserController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberDTO> signup(@Valid @RequestBody MemberDTO memberDTO) {
        return ResponseEntity.ok(loginService.signup(memberDTO));
    }

//    @GetMapping("/user")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
//    public ResponseEntity<MemberDTO> getMyUserInfo(HttpServletRequest request) {
//        return ResponseEntity.ok(loginService.getMyUserWithAuthorities());
//    }

//    @GetMapping("/user/{username}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public ResponseEntity<MemberDTO> getUserInfo(@PathVariable String username) {
//        return ResponseEntity.ok(loginService.getUserWithAuthorities(username));
//    }
}
