package com.RenToU.rentserver.application;


import com.RenToU.rentserver.DTO.MemberDTO;
import com.RenToU.rentserver.domain.Authority;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.MemberAuthority;
import com.RenToU.rentserver.exeption.DuplicateMemberException;
import com.RenToU.rentserver.infrastructure.AuthorityRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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

        makeDefaultRoleIfNotExists();

        Member member = mapper.map(memberDTO,Member.class);
        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        member.setNewUser();
        MemberAuthority memberAuthority = new MemberAuthority();
        Authority user = authorityRepository.findByAuthorityName("USER_ROLE").get();
        member.addMemberAuth(memberAuthority);
        user.addMemberAuth(memberAuthority);
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
    private void makeDefaultRoleIfNotExists(){
        List<Authority> all = authorityRepository.findAll();
        if(all.isEmpty()){
            authorityRepository.save((Authority.createAuth("ADMIN_ROLE")));
            authorityRepository.save((Authority.createAuth("USER_ROLE")));
        }
    }
}

