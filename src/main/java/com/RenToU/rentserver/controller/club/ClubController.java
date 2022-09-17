package com.RenToU.rentserver.controller.club;

import com.RenToU.rentserver.application.ClubService;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.S3Service;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.dto.*;
import com.RenToU.rentserver.dto.response.ClubInfoDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;
    private final MemberService memberService;
    private final S3Service s3Service;
    @Value("${external.mode}")
    private String MODE;

    @PostMapping("")
    public ResponseEntity<?> createClub(@RequestParam("name") String name, @RequestParam("introduction") String intro,
            @RequestParam("thumbnail") MultipartFile thumbnail, @RequestParam("hashtags") List<String> hashtags) {
        long memberId = memberService.getMyIdWithAuthorities();
        String thumbnailPath = "https://ren2u.s3.ap-northeast-2.amazonaws.com/default/club-thumbnail.png";
        if (!thumbnail.isEmpty()) {
            try {
                String randomImgFileName = RandomStringUtils.random(20, true, true) + ".png";
                String filePath = MODE + "/club/" + randomImgFileName;
                thumbnailPath = s3Service.upload(thumbnail, filePath);
            } catch (IOException e) {
                throw new CustomException(ClubErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        Club club = clubService.createClub(memberId, name, intro, thumbnailPath, hashtags);
        ClubInfoDto resData = ClubInfoDto.from(club);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_CLUB, resData));
    }

    @GetMapping("/{clubId}/info")
    public ResponseEntity<?> getClubInfo(@PathVariable long clubId) {
        Club club = clubService.findClubById(clubId);
        ClubInfoDto resData = ClubInfoDto.from(club);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_CLUB, resData));
    }

    @PutMapping("/{clubId}/info")
    public ResponseEntity<?> updateClub(@PathVariable long clubId, @RequestParam("name") String name,
            @RequestParam("introduction") String intro,
            @RequestParam("thumbnail") MultipartFile thumbnail, @RequestParam("hashtags") List<String> hashtags) {
        String thumbnailPath = null;
        if (thumbnail != null && !thumbnail.isEmpty()) {
            try {
                String randomImgFileName = RandomStringUtils.random(10, true, true) + ".png";
                String filePath = MODE + "/club/" + clubId + "/" + randomImgFileName;
                thumbnailPath = s3Service.upload(thumbnail, filePath);
            } catch (IOException e) {
                throw new CustomException(ClubErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }
        Club club = clubService.updateClubInfo(memberService.getMyIdWithAuthorities(), clubId, name, intro,
                thumbnailPath,
                hashtags);
        ClubInfoDto resData = ClubInfoDto.from(club);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.UPDATE_CLUB, resData));
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<?> deleteClub(@PathVariable long clubId) {
        long memberId = memberService.getMyIdWithAuthorities();
        clubService.deleteClub(memberId, clubId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.DELETE_CLUB, null));
    }

}
