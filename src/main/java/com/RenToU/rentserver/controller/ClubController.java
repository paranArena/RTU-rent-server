package com.RenToU.rentserver.controller;

import com.RenToU.rentserver.DTO.*;
import com.RenToU.rentserver.application.ClubServiceImpl;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.S3Service;
import com.RenToU.rentserver.domain.Club;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    private final ClubServiceImpl clubService;
    private final MemberService memberService;
    private final S3Service s3Service;

    @PostMapping("")
    public ResponseEntity<?> createClub(@RequestParam("name") String name, @RequestParam("introduction") String intro, @RequestParam("thumbnail") MultipartFile thumbnail) throws IOException {
        String thumbnailPath = null;
        if(!thumbnail.isEmpty()){
            thumbnailPath = s3Service.upload(thumbnail);
        }
        Club club = clubService.createClub(memberService.getMyIdWithAuthorities(), name, intro, thumbnailPath);
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.CREATED_CLUB, ClubDTO.from(club)), HttpStatus.OK);
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
