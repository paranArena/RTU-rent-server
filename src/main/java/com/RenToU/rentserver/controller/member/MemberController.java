package com.RenToU.rentserver.controller.member;


import com.RenToU.rentserver.dto.RentalDto;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.MemberClubDto;
import com.RenToU.rentserver.dto.response.MemberInfoDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.github.dozermapper.core.Mapper;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Rental;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final Mapper mapper;

    @GetMapping("/my/info")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        Member member = memberService.getMyUserWithAuthorities();
        MemberInfoDto resData = mapper.map(member, MemberInfoDto.class);

        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MEMBER, resData));
    }

    @GetMapping("/my/clubs")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyClubs(HttpServletRequest request) {
        List<ClubMember> clubMembers = memberService.getMyUserWithAuthorities().getClubList();
        List<MemberClubDto> resData = clubMembers.stream()
                                        .map((cm)->MemberClubDto.from(cm))
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MEMBER, resData));
    }

    @GetMapping("/my/rentals")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyRentals(HttpServletRequest request) {
        List<Rental> clubs = memberService.getMyUserWithAuthorities().getRentals();
        List<RentalDto> resData = clubs.stream()
                                        .map((c)->mapper.map(c, RentalDto.class))
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MEMBER, resData));
    }

    @GetMapping("/{email}/info")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?>getMemberInfo(@PathVariable("email") String email) {
        Member member = memberService.getUserWithAuthorities(email);
        MemberInfoDto resData = mapper.map(member, MemberInfoDto.class);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MEMBER, resData));
    }


}
