package com.RenToU.rentserver.controller.member;


import com.RenToU.rentserver.dto.RentalDto;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.MemberClubDto;
import com.RenToU.rentserver.dto.response.MemberInfoDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.response.preview.NotificationPreviewDto;
import com.github.dozermapper.core.Mapper;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.NotificationService;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.domain.Rental;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/my")
public class MyController {
    private final MemberService memberService;
    private final NotificationService notificationService;
    private final Mapper mapper;

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        Member member = memberService.getMyUserWithAuthorities();
        MemberInfoDto resData = mapper.map(member, MemberInfoDto.class);

        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MEMBER, resData));
    }

    @GetMapping("/clubs")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyClubs(HttpServletRequest request) {
        List<ClubMember> clubMembers = memberService.getMyUserWithAuthorities().getClubList();
        List<MemberClubDto> resData = clubMembers.stream()
                                        .map((cm)->MemberClubDto.from(cm))
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MEMBER, resData));
    }

    @GetMapping("/notifications")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMynotifications(HttpServletRequest request) {
        Long memberId = memberService.getMyUserWithAuthorities().getId();
        List<Notification> notifications = notificationService.getMyNotifications(memberId);
        List<NotificationPreviewDto> resData = 
            notifications.stream()
            .map((n)->NotificationPreviewDto.from(n))
            .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_NOTIFICATION, resData));
    }

    @GetMapping("/rentals")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyRentals(HttpServletRequest request) {
        List<Rental> clubs = memberService.getMyUserWithAuthorities().getRentals();
        List<RentalDto> resData = clubs.stream()
                                        .map((c)->mapper.map(c, RentalDto.class))
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_RENT, resData));
    }

    @GetMapping("/clubs/{clubId}/role")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyClubRole(@PathVariable Long clubId) {
        return ResponseEntity.ok("");
    }
}
