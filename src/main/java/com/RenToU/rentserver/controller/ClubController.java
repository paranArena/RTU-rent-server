package com.RenToU.rentserver.controller;

import com.RenToU.rentserver.DTO.*;
import com.RenToU.rentserver.application.ClubServiceImpl;
import com.RenToU.rentserver.application.MemberService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    private final ClubServiceImpl clubService;
    private final MemberService memberService;

    @PostMapping("")
    public ResponseEntity<?> createClub(@Valid @RequestBody ClubDTO clubDTO) {
        clubService.createClub(memberService.getMyIdWithAuthorities(), clubDTO.getName(), clubDTO.getThumbnailPath(),clubDTO.getIntroduction());
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.CREATED_CLUB, null), HttpStatus.OK);
    }

    @PostMapping("/{club_id}/requests/join")
    public ResponseEntity<?> requestClubJoin(@PathVariable("club_id") Long clubId) {
        clubService.requestClubJoin(clubId, memberService.getMyIdWithAuthorities());
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.REQUEST_CLUB_JOIN, null), HttpStatus.OK);
    }

    @PostMapping("/{club_id}/requests/join/{join_member_id}")
    public ResponseEntity<?> acceptClubJoin(
        @PathVariable("club_id") Long clubId, @PathVariable("join_member_id") Long joinMemberId
        ) {
        clubService.acceptClubJoin(clubId, memberService.getMyIdWithAuthorities(), joinMemberId);
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.ACCEPT_CLUB_JOIN, null), HttpStatus.OK);
    }
}
