package com.RenToU.rentserver.controller.club;

import com.RenToU.rentserver.application.ClubServiceImpl;
import com.RenToU.rentserver.application.HashtagService;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.dto.*;
import com.RenToU.rentserver.dto.response.ClubInfoDto;
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

    private final ClubServiceImpl clubService;
    private final HashtagService hashtagService;

    @GetMapping("")
    public ResponseEntity<?> searchlubWithHashtag(@RequestParam Map<String,String> searchMap) {
        if(searchMap.containsKey("name")){
            String clubName = searchMap.get("name");
            Club club = clubService.findClubByName(clubName);
            ClubInfoDto resData = ClubInfoDto.from(club);
            return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_SUCCESS, resData));
        }
        else if (searchMap.containsKey("hashtag")){
            String hashtag = searchMap.get("hashtag");
            List<Club> clubs = hashtagService.findClubsWithHashtag(hashtag);
            List<ClubInfoDto> resData = 
                clubs.stream()
                .map(club->ClubInfoDto.from(club))
                .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_SUCCESS, resData));
        }
        return ResponseEntity.ok(ResponseDto.res(StatusCode.BAD_REQUEST, ResponseMessage.SEARCH_CLUB_FAIL));
    }

    @GetMapping("/all")
    public ResponseEntity<?> searchAllClubs(){
        List<Club> clubs = clubService.findClubs();
        List<ClubInfoDto> resData = clubs.stream()
            .map(club->ClubInfoDto.from(club))
            .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_SUCCESS, resData));
    }
}
