package com.RenToU.rentserver.application;


import com.RenToU.rentserver.DTO.MemberDTO;
import com.RenToU.rentserver.domain.Authority;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.MemberAuthority;
import com.RenToU.rentserver.exeption.DuplicateMemberException;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public MemberDTO signup(MemberDTO memberDTO) {
        if (memberRepository.findByEmail(memberDTO.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Member member = Member.builder()
                .activated(true)
                .email(memberDTO.getEmail())
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .name(memberDTO.getName())
                .phoneNumber(memberDTO.getPhoneNumber())
                .studentId(memberDTO.getStudentId())
                .major(memberDTO.getMajor())
                .build();

        return MemberDTO.from(memberRepository.save(member));
    }

//    @Transactional(readOnly = true)
//    public MemberDTO getMemberWithAuthorities(String email) {
//        return MemberDTO.from(memberRepository.findOneWithAuthoritiesByUsername(email).orElse(null));
//    }

//    @Transactional(readOnly = true)
//    public MemberDTO getMyUserWithAuthorities() {
//        return MemberDTO.from(SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithAuthoritiesByUsername).orElse(null));
//    }
}
