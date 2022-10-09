package com.RenToU.rentserver.application;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.dto.request.EmailDto;
import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CommonErrorCode;
import com.RenToU.rentserver.infrastructure.ClubMemberRepository;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.util.RedisUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RenToU.rentserver.domain.Authority;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.dto.request.SignupDto;
import com.RenToU.rentserver.dto.request.UpdateMemberInfoDto;
import com.RenToU.rentserver.exceptions.AuthErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;
import com.RenToU.rentserver.exceptions.MemberErrorCode;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.RenToU.rentserver.domain.ClubRole.ADMIN;
import static com.RenToU.rentserver.domain.ClubRole.OWNER;
import static com.RenToU.rentserver.domain.ClubRole.USER;
import static com.RenToU.rentserver.domain.ClubRole.WAIT;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Value("${external.mode}")
    private String MODE;

    @Transactional
    public Member signup(SignupDto signupDTO) {
        if (memberRepository.existsByEmail(signupDTO.getEmail())) {
            throw new CustomException(MemberErrorCode.DUP_EMAIL);
        }
        if (memberRepository.existsByPhoneNumber(signupDTO.getPhoneNumber())) {
            throw new CustomException(MemberErrorCode.DUP_PHONE);
        }
        if (memberRepository.existsByStudentId(signupDTO.getStudentId())) {
            throw new CustomException(MemberErrorCode.DUP_STUDENTID);
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

        memberRepository.save(member);
        Club ren2u = clubRepository.findById(19L).get();
        clubMemberRepository.save(ClubMember.createClubMember(ren2u, member, USER));
        return member;
    }

    @Transactional
    public Member updateMemberInfo(Long memberId, UpdateMemberInfoDto info) {
        checkPhoneDuplicate(memberId, info.getPhoneNumber());
        checkStudentIdDuplicate(memberId, info.getStudentId());
        Member member = findMember(memberId);
        member.setMajor(info.getMajor());
        member.setName(info.getName());
        member.setPhoneNumber(info.getPhoneNumber());
        member.setStudentId(info.getStudentId());
        return memberRepository.save(member);
    }

    private void checkStudentIdDuplicate(Long memberId, String studentId) {
        Optional<Member> member = memberRepository.findByStudentId(studentId);
        if (member.isPresent() && member.get().getId() != memberId)
            throw new CustomException(MemberErrorCode.DUP_STUDENTID);
    }

    private void checkPhoneDuplicate(Long memberId, String phoneNumber) {
        Optional<Member> member = memberRepository.findByPhoneNumber(phoneNumber);
        if (member.isPresent() && member.get().getId() != memberId)
            throw new CustomException(MemberErrorCode.DUP_PHONE);
    }

    public void resetPassword(Long memberId, String pw) {
        Member member = findMember(memberId);
        member.setPassword(passwordEncoder.encode(pw));
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member getUserWithAuthorities(String email) {
        return memberRepository.findOneWithAuthoritiesByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Member getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public long getMyIdWithAuthorities() {
        Member member = SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        return member.getId();
    }

    @Transactional(readOnly = true)
    public boolean checkEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean checkPhoneDuplicate(String phone) {
        return memberRepository.existsByPhoneNumber(phone);
    }

    public boolean checkStudentIdDuplicate(String studentId) {
        return memberRepository.existsByStudentId(studentId);
    }

    public void authEmail(EmailDto request) {
        // 임의의 authKey 생성
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111);// 범위 : 111111 ~ 999999

        // 이메일 발송
        if (MODE.equals("prod")) {
            sendAuthEmail(request.getEmail(), authKey);
        } else {
            sendAuthEmail(request.getEmail(), "111111");
        }
    }

    private void sendAuthEmail(String email, String authKey) {

        String subject = "Ren2U - Email Verification";
        String text = "<h2>Ren2U 이메일 인증 코드</h2>"
                + "<p>이메일 인증 코드 : <strong>" + authKey
                + "</strong></p><p>인증 코드는 5분 후에 만료됩니다. 최대한 빠른 시간내에 인증 부탁드리겠습니다.</p>"
                + "<p>인증이 원활히 이루어지지 않을 시 다시 로그인을 시도해주세요.</p>";
        checkAjouEmail(email);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true); // 포함된 텍스트가 HTML이라는 의미로 true.
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new CustomException(CommonErrorCode.EMAIL_SEND_FAILED);
        }

        // 유효 시간(5분)동안 {email, authKey} 저장
        redisUtil.setDataExpire(email, authKey, 60 * 5L);
    }

    public void verifyCode(String key, String input) {
        String value = redisUtil.getData(key);
        System.out.println(input + " " + value);
        if (!input.equals(value)) {
            throw new CustomException(AuthErrorCode.WRONG_VERIFICATION_CODE);
        }
    }

    private void checkAjouEmail(String email) {
        int idx = email.indexOf("@");
        String domain = email.substring(idx + 1);
        if (!domain.equals("ajou.ac.kr")) {
            throw new CustomException(AuthErrorCode.NOT_AJOU_EMAIL);
        }
    }

    private Member findMemberByEMail(String email) {
        Member member = memberRepository.findOneWithAuthoritiesByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        return member;
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = findMember(memberId);
        try {
            member.getClubList().stream().forEach(cm -> cm.validateRole(false, OWNER));// not OWNER
        } catch (CustomException e) {
            throw new CustomException(ClubErrorCode.CLUB_OWNER_CANT_QUIT);
        }
        member.toTempMember();
        memberRepository.save(member);
    }

    public Member findByStudentId(String studentId) {
        return memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

}
