package com.RenToU.rentserver.controller.member;

import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.EmailDto;
import com.RenToU.rentserver.dto.request.EmailVerifyDto;
import com.RenToU.rentserver.dto.request.LoginDto;
import com.RenToU.rentserver.dto.request.SignupDto;
import com.RenToU.rentserver.dto.response.MemberInfoDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.response.TokenDto;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.jwt.JwtFilter;
import com.RenToU.rentserver.jwt.TokenProvider;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;
    private final Mapper mapper;

    @GetMapping("/")
    public ResponseEntity<String> hello(HttpServletRequest request) {
        return ResponseEntity.ok("<h1> Server is Running :) </h1>");
    }

    @GetMapping("/members/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable("email") String email) {

        return ResponseEntity.ok(memberService.checkEmailDuplicate(email));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupDTO) {
        Member member = memberService.signup(signupDTO);
        MemberInfoDto resData = mapper.map(member, MemberInfoDto.class);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_USER, resData));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("members/email/requestCode")
    public ResponseEntity<Void> authEmail(@RequestBody @Valid EmailDto request) {
        memberService.authEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("members/email/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody @Valid EmailVerifyDto request) {
        memberService.verifyCode(request);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.EMAIL_VERIFIED, null));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // TODO ?
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
