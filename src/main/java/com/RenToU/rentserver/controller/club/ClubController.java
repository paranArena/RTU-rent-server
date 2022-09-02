package com.RenToU.rentserver.controller.club;

import com.RenToU.rentserver.application.ClubService;
import com.RenToU.rentserver.application.HashtagService;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.S3Service;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.dto.*;
import com.RenToU.rentserver.dto.response.ClubInfoDto;
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

    private final ClubService clubService;
    private final MemberService memberService;
    private final HashtagService hashtagService;
    private final S3Service s3Service;
    private final Mapper mapper;

    @PostMapping("")
    public ResponseEntity<?> createClub(@RequestParam("name") String name, @RequestParam("introduction") String intro, @RequestParam("thumbnail") MultipartFile thumbnail,@RequestParam("hashtags") List<String> hashtags) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        String thumbnailPath = "https://ren2u.s3.ap-northeast-2.amazonaws.com/images.jpg";
        if(!thumbnail.isEmpty()) {
            thumbnailPath = s3Service.upload(thumbnail);
        }
        
        Club club = clubService.createClub(memberId, name, intro, thumbnailPath, hashtags);
        ClubInfoDto resData = ClubInfoDto.from(club);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_CLUB, resData));
    }

    @GetMapping("/{clubId}/info")
    public ResponseEntity<?> getClub(@PathVariable long clubId) {
        Club club = clubService.findClubById(clubId);
        ClubInfoDto resData = mapper.map(club, ClubInfoDto.class);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_CLUB, resData));
    }

    @PutMapping("/{clubId}")
    public ResponseEntity<?> updateClub(@RequestParam("name") String name, @RequestParam("introduction") String intro, @RequestParam("thumbnail") MultipartFile thumbnail,@RequestParam("hashtags") List<String> hashtags) throws IOException {
        String thumbnailPath = null;
        if(!thumbnail.isEmpty()){
            thumbnailPath = s3Service.upload(thumbnail);
        }
        Club club = clubService.createClub(memberService.getMyIdWithAuthorities(), name, intro, thumbnailPath,hashtags);
        ClubInfoDto resData = mapper.map(club, ClubInfoDto.class);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.UPDATE_CLUB, resData));
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<?> deleteClub(@PathVariable long clubId) {
        long memberId = memberService.getMyIdWithAuthorities();
        clubService.deleteClub(memberId, clubId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.DELETE_CLUB, null));
    }


}
