package com.RenToU.rentserver.application;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RenToU.rentserver.DTO.MemberDTO;
import com.RenToU.rentserver.domain.Authority;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.exceptions.DuplicateMemberException;
import com.RenToU.rentserver.exceptions.NotFoundMemberException;
import com.RenToU.rentserver.infrastructure.AuthorityRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberDTO signup(MemberDTO memberDto) {
        if (memberRepository.findOneWithAuthoritiesByEmail(memberDto.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 존재하는 이메일입니다.");
        }
        if (memberRepository.findOneWithAuthoritiesByPhoneNumber(memberDto.getPhoneNumber()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 존재하는 휴대폰 번호입니다.");
        }
        if (memberRepository.findOneWithAuthoritiesByStudentId(memberDto.getStudentId()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 존재하는 학번입니다.");
        }


        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .name(memberDto.getName())
                .phoneNumber(memberDto.getPhoneNumber())
                .studentId(memberDto.getStudentId())
                .major(memberDto.getMajor())
                .activated(true)
                // .clubList()
                // .rentals()
                .authorities(Collections.singleton(authority))
                .build();

        return MemberDTO.from(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberDTO getUserWithAuthorities(String email) {
        return MemberDTO.from(memberRepository.findOneWithAuthoritiesByEmail(email).orElse(null));
    }

    @Transactional(readOnly = true)
    public MemberDTO getMyUserWithAuthorities() {
        return MemberDTO.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }

    @Transactional(readOnly = true)
    public long getMyIdWithAuthorities() {
        Member member = SecurityUtil.getCurrentUsername()
                        .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"));
        return member.getId();
    }
    
    @Transactional(readOnly = true)
    private void makeDefaultRoleIfNotExists(){
        List<Authority> all = authorityRepository.findAll();
        if(all.isEmpty()){
            authorityRepository.save((Authority.builder().authorityName("ROLE_ADMIN").build()));
            authorityRepository.save((Authority.builder().authorityName("ROLE_USER").build()));
        }
    }
}


