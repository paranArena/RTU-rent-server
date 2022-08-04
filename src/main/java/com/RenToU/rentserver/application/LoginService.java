package com.RenToU.rentserver.application;


import com.RenToU.rentserver.DTO.MemberDTO;
import com.RenToU.rentserver.domain.Authority;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.MemberAuthority;
import com.RenToU.rentserver.exceptions.DuplicateMemberException;
import com.RenToU.rentserver.infrastructure.AuthorityRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.util.SecurityUtil;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Service
@RequiredArgsConstructor
public class LoginService {
    private final Mapper mapper;
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public MemberDTO signup(MemberDTO memberDTO) {
        if (memberRepository.findByEmail(memberDTO.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        // for dev
        makeDefaultRoleIfNotExists();

        Member member = mapper.map(memberDTO,Member.class);
        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        member.setNewUser();
        MemberAuthority memberAuthority = new MemberAuthority();
        Authority authority = authorityRepository.findByAuthorityName("ROLE_USER").get();
        member.addMemberAuth(memberAuthority);
        authority.addMemberAuth(memberAuthority);
        return MemberDTO.from(memberRepository.save(member));
    }

//    @Transactional(readOnly = true)
//    public MemberDTO getMemberWithAuthorities(String email) {
//        return MemberDTO.from(memberRepository.findOneWithAuthoritiesByUsername(email).orElse(null));
//    }
//
    @Transactional(readOnly = true)
    public MemberDTO getMyUserWithAuthorities() {
        return MemberDTO.from(SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithAuthoritiesByEmail).orElse(null));
    }

    private void makeDefaultRoleIfNotExists(){
        List<Authority> all = authorityRepository.findAll();
        if(all.isEmpty()){
            authorityRepository.save((Authority.createAuth("ROLE_ADMIN")));
            authorityRepository.save((Authority.createAuth("ROLE_USER")));
        }
    }
}

