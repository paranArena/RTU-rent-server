package com.RenToU.rentserver.application;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.RenToU.rentserver.dto.request.EmailDto;
import com.RenToU.rentserver.dto.request.EmailVerifyDto;
import com.RenToU.rentserver.exceptions.NotAjouEmailException;
import com.RenToU.rentserver.exceptions.WrongEmailCodeException;
import com.RenToU.rentserver.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RenToU.rentserver.domain.Authority;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.dto.request.SignupDto;
import com.RenToU.rentserver.exceptions.DuplicateMemberException;
import com.RenToU.rentserver.exceptions.NotFoundMemberException;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    @Transactional
    public Member signup(SignupDto signupDTO) {
        if (memberRepository.existsByEmail(signupDTO.getEmail())) {
            throw new DuplicateMemberException("이미 존재하는 이메일입니다.");
        }
        if (memberRepository.existsByPhoneNumber(signupDTO.getPhoneNumber())) {
            throw new DuplicateMemberException("이미 존재하는 휴대폰 번호입니다.");
        }
        if (memberRepository.existsByStudentId(signupDTO.getStudentId())) {
            throw new DuplicateMemberException("이미 존재하는 학번입니다.");
        }


        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .email(signupDTO.getEmail())
                .password(passwordEncoder.encode(signupDTO.getPassword()))
                .name(signupDTO.getName())
                .phoneNumber(signupDTO.getPhoneNumber())
                .studentId(signupDTO.getStudentId())
                .major(signupDTO.getMajor())
                .activated(true)
                .authorities(Collections.singleton(authority))
                .build();

        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member getUserWithAuthorities(String email) {
        return memberRepository.findOneWithAuthoritiesByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public Member getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));
    }

    @Transactional(readOnly = true)
    public long getMyIdWithAuthorities() {
        Member member = SecurityUtil.getCurrentUsername()
                        .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"));
        return member.getId();
    }

    @Transactional(readOnly = true)
    public boolean checkEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }

    public void authEmail(EmailDto request) {
        // 임의의 authKey 생성
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111);// 범위 : 111111 ~ 999999

        // 이메일 발송
        sendAuthEmail(request.getEmail(), authKey);
    }

    private void sendAuthEmail(String email, String authKey) {

        String subject = "제목";
        String text = "회원 가입을 위한 인증번호는 " + authKey + "입니다. <br/>";
        checkAjouEmail(email);
        try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
                helper.setTo(email);
                helper.setSubject(subject);
                helper.setText(text, true);    //포함된 텍스트가 HTML이라는 의미로 true.
                javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
                e.printStackTrace();
        }

        // 유효 시간(5분)동안 {email, authKey} 저장
        redisUtil.setDataExpire(email, authKey, 60 * 5L);
    }



    public void verifyCode(EmailVerifyDto request) {
        String userCode = request.getCode();
        String email = request.getEmail();
        String verifyCode = redisUtil.getData(email);
        System.out.println(userCode +" "+ verifyCode);
        if(!userCode.equals(verifyCode)){
            throw new WrongEmailCodeException();
        }
    }
    private void checkAjouEmail(String email) {
        int idx = email.indexOf("@");
        String domain = email.substring(idx+1);
        if(!domain.equals("ajou.ac.kr")){
            throw new NotAjouEmailException();
        }

    }
}


