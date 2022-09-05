package com.RenToU.rentserver.application;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.exceptions.MemberNotActivatedException;
import com.RenToU.rentserver.infrastructure.MemberRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
   private final MemberRepository memberRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String email) {
      return memberRepository.findOneWithAuthoritiesByEmail(email)
            .map(member -> createMember(email, member))
            .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
   }

   private org.springframework.security.core.userdetails.User createMember(String email, Member member) {
      if (!member.isActivated()) {
         throw new MemberNotActivatedException(email + " -> 활성화되어 있지 않습니다.");
      }

      List<GrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
            .collect(Collectors.toList());

      return new org.springframework.security.core.userdetails.User(member.getEmail(),
            member.getPassword(),
            grantedAuthorities);
   }
}
