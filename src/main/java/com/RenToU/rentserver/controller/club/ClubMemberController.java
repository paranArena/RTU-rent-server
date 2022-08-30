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
@RequestMapping("/clubs/{clubId}/members")
public class ClubMemberController {

    private final ClubService clubService;
    private final MemberService memberService;
    private final HashtagService hashtagService;
    private final S3Service s3Service;
    private final Mapper mapper;

    // @GetMapping("/{memberId}")
    // public ResponseEntity<?> getClubMember(@PathVariable long clubId, @PathVariable long memberId) {
    //     ClubMember clubMember = clubService.findClubById(clubId).getMemberList();
    //     ClubInfoDto resData = mapper.map(club, ClubInfoDto.class);
    //     return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_CLUB, resData));
    // }

    // @GetMapping("/search/all")
    // public ResponseEntity<?> searchClubMembersAll() {
    //     Club club = clubService.;
    //     ClubInfoDto resData = mapper.map(club, ClubInfoDto.class);
    //     return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_CLUB, resData));
    // }
}

