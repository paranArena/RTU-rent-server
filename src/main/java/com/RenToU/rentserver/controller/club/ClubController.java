package com.RenToU.rentserver.controller.club;

import com.RenToU.rentserver.application.ClubServiceImpl;
import com.RenToU.rentserver.application.HashtagService;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.S3Service;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.dto.*;
import com.RenToU.rentserver.dto.response.ClubDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    private final ClubServiceImpl clubService;
    private final MemberService memberService;
    private final HashtagService hashtagService;
    private final S3Service s3Service;
    private final Mapper mapper;

    @PostMapping("")
    public ResponseEntity<?> createClub(@RequestParam("name") String name, @RequestParam("introduction") String intro, @RequestParam("thumbnail") MultipartFile thumbnail,@RequestParam("hashtags") List<String> hashtags) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        String thumbnailPath = null;
        if(!thumbnail.isEmpty()){
            thumbnailPath = s3Service.upload(thumbnail);
        }
        
        Club club = clubService.createClub(memberId, name, intro, thumbnailPath, hashtags);
        return new ResponseEntity<>(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_CLUB, ClubDto.from(club)), HttpStatus.OK);
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<?> getClub(@PathVariable long clubId) {
        Club club = clubService.findClubById(clubId);
        return new ResponseEntity<>(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_CLUB, ClubDto.from(club)), HttpStatus.OK);
    }

    @PutMapping("/{clubId}")
    public ResponseEntity<?> updateClub(@RequestParam("name") String name, @RequestParam("introduction") String intro, @RequestParam("thumbnail") MultipartFile thumbnail,@RequestParam("hashtags") List<String> hashtags) throws IOException {
        String thumbnailPath = null;
        if(!thumbnail.isEmpty()){
            thumbnailPath = s3Service.upload(thumbnail);
        }
        Club club = clubService.createClub(memberService.getMyIdWithAuthorities(), name, intro, thumbnailPath,hashtags);
        return new ResponseEntity<>(ResponseDto.res(StatusCode.OK, ResponseMessage.UPDATE_CLUB, ClubDto.from(club)), HttpStatus.OK);
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<?> deleteClub(@PathVariable long clubId) {
        // clubService.deleteClub(clubId);
        return new ResponseEntity<>(ResponseDto.res(StatusCode.OK, ResponseMessage.DELETE_CLUB, null), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> searchlubWithHashtag(@RequestParam Map<String,String> searchMap) {
        if(searchMap.containsKey("name")){
            String clubName = searchMap.get("name");
            Club club = clubService.findClubByName(clubName);
            return new ResponseEntity<>(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_SUCCESS, ClubDto.from(club)), HttpStatus.OK);
        }
        else if (searchMap.containsKey("hashtag")){
            String hashtag = searchMap.get("hashtag");
            List<Club> clubs = hashtagService.findClubsWithHashtag(hashtag);
            List<ClubDto> clubDtos = clubs.stream()
            .map(club->ClubDto.from(club))
            .collect(Collectors.toList());
            return new ResponseEntity<>(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_SUCCESS, clubDtos), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseDto.res(StatusCode.BAD_REQUEST, ResponseMessage.SEARCH_CLUB_FAIL), HttpStatus.BAD_REQUEST);
    }
}
