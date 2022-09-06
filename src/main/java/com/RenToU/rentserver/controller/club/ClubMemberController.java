package com.RenToU.rentserver.controller.club;

import com.RenToU.rentserver.application.ClubService;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.ClubMemberDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/members")
public class ClubMemberController {

    private final ClubService clubService;
    private final MemberService memberService;

    // TODO fix
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getClubMember(@PathVariable long clubId, @PathVariable long memberId) {
        List<ClubMember> allClubMembers = clubService.getAllMembers(clubId);
        ClubMember target = allClubMembers.stream()
                .filter((cm) -> cm.getMember().getId() == memberId)
                .collect(Collectors.toList())
                .get(0); // FIXEME: java.lang.IndexOutOfBoundsException
        ClubMemberDto resData = ClubMemberDto.from(target);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_CLUB_MEMBER, resData));
    }

    @GetMapping("/search/all")
    public ResponseEntity<?> searchClubMembersAll(@PathVariable Long clubId) {
        List<ClubMember> allClubMembers = clubService.getAllMembers(clubId);
        List<ClubMemberDto> resData = allClubMembers.stream()
                .map((cm) -> ClubMemberDto.from(cm))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_CLUB_MEMBER, resData));
    }

    @PutMapping(value = "{memberId}/role/admin")
    public ResponseEntity<?> grantAdmin(@PathVariable Long clubId, @PathVariable Long memberId) {
        Long ownerId = memberService.getMyIdWithAuthorities();
        clubService.grantAdmin(clubId, ownerId, memberId);

        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GRANT_ADMIN));
    }

    @PutMapping(value = "{memberId}/role/user")
    public ResponseEntity<?> grantUser(@PathVariable Long clubId, @PathVariable Long memberId) {
        Long ownerId = memberService.getMyIdWithAuthorities();
        clubService.grantUser(clubId, ownerId, memberId);

        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GRANT_USER));
    }
}
