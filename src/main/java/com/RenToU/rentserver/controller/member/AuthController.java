package com.RenToU.rentserver.controller.member;

import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.EmailDto;
import com.RenToU.rentserver.dto.request.EmailVerifyDto;
import com.RenToU.rentserver.dto.request.LoginDto;
import com.RenToU.rentserver.dto.request.PasswordDto;
import com.RenToU.rentserver.dto.request.SignupDto;
import com.RenToU.rentserver.dto.response.MemberInfoDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.response.TokenDto;
import com.RenToU.rentserver.exceptions.AuthErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;
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
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupDto) {
        memberService.verifyCode(signupDto.getEmail(), signupDto.getVerificationCode());
        Member member = memberService.signup(signupDto);
        MemberInfoDto resData = mapper.map(member, MemberInfoDto.class);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_USER, resData));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) throws CustomException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword());
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.createToken(authentication);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("활성화되지 않은 유저입니다."))
                throw new CustomException(AuthErrorCode.INACTIVE_USER);
            else
                throw e;
        }
    }

    @PostMapping("members/email/requestCode")
    public ResponseEntity<?> authEmail(@RequestBody @Valid EmailDto request) {
        memberService.authEmail(request);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.REQUEST_EMAIL_VERIFICATION));
    }

    @PostMapping("members/email/verifyCode")
    public ResponseEntity<?> activateMember(@RequestBody @Valid EmailVerifyDto request) {
        memberService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.EMAIL_VERIFIED));
    }

    @PostMapping("pawssord/reset/verify")
    public ResponseEntity<?> resetPasswordWithVerifyCode(@RequestBody @Valid EmailVerifyDto request) {
        memberService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.EMAIL_VERIFIED));
    }

    @PutMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordDto dto) {
        Long memberId = memberService.getMyIdWithAuthorities();
        memberService.resetPassword(memberId, dto.getPassword());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RESET_PASSWORD));
    }

}
