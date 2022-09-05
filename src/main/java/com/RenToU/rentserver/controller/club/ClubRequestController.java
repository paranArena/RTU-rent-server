package com.RenToU.rentserver.controller.club;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RenToU.rentserver.application.ClubService;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.ClubMemberDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/requests")
public class ClubRequestController {

    private final MemberService memberService;
    private final ClubService clubService;

    @PostMapping("/join")
    public ResponseEntity<?> requestClubJoin(@PathVariable Long clubId) {
        clubService.requestClubJoin(clubId, memberService.getMyIdWithAuthorities());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.REQUEST_CLUB_JOIN));
    }

    @PostMapping("/join/{joinMemberId}")
    public ResponseEntity<?> acceptClubJoin(
            @PathVariable Long clubId,
            @PathVariable Long joinMemberId) {
        clubService.acceptClubJoin(clubId, memberService.getMyIdWithAuthorities(), joinMemberId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.ACCEPT_CLUB_JOIN));
    }

    @DeleteMapping("/join/cancel")
    public ResponseEntity<?> cancelClubJoin(
            @PathVariable Long clubId) {
        clubService.cancelClubJoin(clubId, memberService.getMyIdWithAuthorities());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CANCEL_CLUB_JOIN));
    }

    @GetMapping("/join/search/all")
    public ResponseEntity<?> searchClubJoinsAll(@PathVariable Long clubId) {
        List<ClubMember> awaitClubMembers = clubService.searchClubJoinsAll(clubId,
                memberService.getMyIdWithAuthorities());
        List<ClubMemberDto> resData = awaitClubMembers.stream()
                .map((cm) -> ClubMemberDto.from(cm))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_CLUB_JOIN, resData));
    }
    @DeleteMapping("/leave")
    public ResponseEntity<?> leaveClub(
            @PathVariable Long clubId
    ) {
        clubService.leaveClub(clubId, memberService.getMyIdWithAuthorities());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.LEAVE_CLUB_SUCCESS));
    }
}