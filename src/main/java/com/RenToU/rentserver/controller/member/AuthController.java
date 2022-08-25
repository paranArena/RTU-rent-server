package com.RenToU.rentserver.controller.member;

import com.RenToU.rentserver.DTO.StatusCode;
import com.RenToU.rentserver.DTO.request.LoginDto;
import com.RenToU.rentserver.DTO.request.SignupDto;
import com.RenToU.rentserver.DTO.response.MemberDto;
import com.RenToU.rentserver.DTO.response.ResponseDto;
import com.RenToU.rentserver.DTO.response.ResponseMessage;
import com.RenToU.rentserver.DTO.response.TokenDto;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.jwt.JwtFilter;
import com.RenToU.rentserver.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/")
    public ResponseEntity<String> hello(HttpServletRequest request) {
        return ResponseEntity.ok("<h1> Server is Running :) </h1>");
    }

//    @PostMapping("/test-redirect")
//    public void testRedirect(HttpServletResponse response) throws IOException {
//        response.sendRedirect("/api/user");
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto signupDTO) {
        Member member = memberService.signup(signupDTO);
        return new ResponseEntity<>(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_USER, MemberDto.from(member) ), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public  ResponseEntity<?> logout(){
        // TODO ?
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
