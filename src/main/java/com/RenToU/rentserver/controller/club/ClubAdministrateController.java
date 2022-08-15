package com.RenToU.rentserver.controller.club;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RenToU.rentserver.DTO.ResponseDTO;
import com.RenToU.rentserver.DTO.ResponseMessage;
import com.RenToU.rentserver.DTO.StatusCode;
import com.RenToU.rentserver.application.ClubService;
import com.RenToU.rentserver.application.MemberService;

import lombok.RequiredArgsConstructor;

/**
 * ClubAdministrateController
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/requests")
public class ClubAdministrateController {

    private final MemberService memberService;
    private final ClubService clubService;

    @PostMapping("/join")
    public ResponseEntity<?> requestClubJoin(@PathVariable Long clubId) {
        clubService.requestClubJoin(clubId, memberService.getMyIdWithAuthorities());
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.REQUEST_CLUB_JOIN, null), HttpStatus.OK);
    }

    @PostMapping("/join/{joinMemberId}")
    public ResponseEntity<?> acceptClubJoin(
        @PathVariable Long clubId,
        @PathVariable Long joinMemberId
        ) {
        clubService.acceptClubJoin(clubId, memberService.getMyIdWithAuthorities(), joinMemberId);
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.ACCEPT_CLUB_JOIN, null), HttpStatus.OK);
    }
    
}