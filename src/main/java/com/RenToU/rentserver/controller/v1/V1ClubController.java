package com.RenToU.rentserver.controller.v1;

import com.RenToU.rentserver.application.ClubService;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.S3Service;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.V1CreateClubDto;
import com.RenToU.rentserver.dto.request.V1UpdateClubDto;
import com.RenToU.rentserver.dto.response.ClubInfoDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

// TODO Caused by: java.sql.SQLException: Field 'd' doesn't have a default value
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/clubs")
public class V1ClubController {

    private final ClubService clubService;
    private final MemberService memberService;
    private final S3Service s3Service;
    @Value("${external.mode}")
    private String MODE;

    @PostMapping("")
    public ResponseEntity<?> createClub(@Valid @RequestBody V1CreateClubDto createClubDto) {
        long memberId = memberService.getMyIdWithAuthorities();
        List<String> imagePaths = createClubDto.getImagePaths();
        String imagePath = null;
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePath = imagePaths.get(0);
        }
        Club club = clubService.createClub(memberId, createClubDto.getName(), createClubDto.getIntro(), imagePath, createClubDto.getHashtags());
        ClubInfoDto resData = ClubInfoDto.from(club);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_CLUB, resData));
    }

    @PutMapping("/{clubId}/info")
    public ResponseEntity<?> updateClub(@PathVariable long clubId, @Valid @RequestBody V1UpdateClubDto updateClubDto) {
        long memberId = memberService.getMyIdWithAuthorities();
        List<String> imagePaths = updateClubDto.getImagePaths();
        String imagePath = null;
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePath = imagePaths.get(0);
        }
        Club club = clubService.updateClubInfo(memberId, clubId, updateClubDto.getName(), updateClubDto.getIntro(),
                imagePath,
                updateClubDto.getHashtags());
        ClubInfoDto resData = ClubInfoDto.from(club);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.UPDATE_CLUB, resData));
    }
}
