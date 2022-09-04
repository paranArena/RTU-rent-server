package com.RenToU.rentserver.controller.club;

import com.RenToU.rentserver.application.ClubService;
import com.RenToU.rentserver.application.HashtagService;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.dto.*;
import com.RenToU.rentserver.dto.response.preview.ClubPreviewDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/search")
public class ClubSearchController {

    private final MemberService memberService;
    private final ClubService clubService;
    private final HashtagService hashtagService;

    @GetMapping("")
    public ResponseEntity<?> searchlubWithHashtag(@RequestParam Map<String, String> searchMap) {
        Long memberId = memberService.getMyIdWithAuthorities();

        if (searchMap.containsKey("name")) {
            String clubName = searchMap.get("name");
            Club club = clubService.findClubByName(clubName);
            ClubPreviewDto resData = ClubPreviewDto.from(club);
            resData.setClubRole(clubService.getMyRole(memberId, club.getId()));
            return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_SUCCESS, resData));
        } else if (searchMap.containsKey("hashtag")) {
            String hashtag = searchMap.get("hashtag");
            List<Club> clubs = hashtagService.findClubsWithHashtag(hashtag);
            List<ClubPreviewDto> resData = clubs.stream()
                    .map(club -> {
                        ClubPreviewDto dto = ClubPreviewDto.from(club);
                        dto.setClubRole(clubService.getMyRole(memberId, club.getId()));
                        return dto;
                    }).collect(Collectors.toList());
            return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_SUCCESS, resData));
        }
        return ResponseEntity.ok(ResponseDto.res(StatusCode.BAD_REQUEST, ResponseMessage.SEARCH_CLUB_FAIL));
    }

    @GetMapping("/all")
    public ResponseEntity<?> searchClubsAll() {
        Long memberId = memberService.getMyIdWithAuthorities();
        List<Club> clubs = clubService.findClubs();
        List<ClubPreviewDto> resData = clubs.stream()
                .map(club -> {
                    ClubPreviewDto dto = ClubPreviewDto.from(club);
                    dto.setClubRole(clubService.getMyRole(memberId, club.getId()));
                    return dto;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_SUCCESS, resData));
    }
}
