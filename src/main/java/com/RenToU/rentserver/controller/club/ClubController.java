package com.RenToU.rentserver.controller.club;

import com.RenToU.rentserver.DTO.*;
import com.RenToU.rentserver.application.ClubServiceImpl;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.S3Service;

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
        ClubDTO clubDto = clubService.createClub(memberService.getMyIdWithAuthorities(), name, intro, thumbnailPath);
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.CREATE_CLUB, clubDto), HttpStatus.OK);
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<?> getClubInfo(@PathVariable long clubId) {
        // Club club = clubService.findClub(clubId);
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.GET_CLUB, null), HttpStatus.OK);
    }

    @PutMapping("/{clubId}")
    public ResponseEntity<?> updateClubInfo(@RequestParam("name") String name, @RequestParam("introduction") String intro, @RequestParam("thumbnail") MultipartFile thumbnail) throws IOException {
        String thumbnailPath = null;
        if(!thumbnail.isEmpty()){
            thumbnailPath = s3Service.upload(thumbnail);
        }
        ClubDTO clubDto = clubService.createClub(memberService.getMyIdWithAuthorities(), name, intro, thumbnailPath);
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.UPDATE_CLUB, clubDto), HttpStatus.OK);
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<?> deleteClub(@PathVariable long clubId) {
        // clubService.deleteClub(clubId);
        return new ResponseEntity<>(ResponseDTO.res(StatusCode.OK, ResponseMessage.DELETE_CLUB, null), HttpStatus.OK);
    }


}
