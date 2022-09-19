package com.RenToU.rentserver.controller.util;

import com.RenToU.rentserver.application.S3Service;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final S3Service s3Service;
    @Value("${external.mode}")
    private String MODE;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImageS3(@RequestParam("image") MultipartFile image) throws IOException {
        String randomImgFileName = RandomStringUtils.random(20, true, true) + ".png";
        String filePath = MODE + "/images/" + randomImgFileName;
        String resData = s3Service.upload(image, filePath);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_RETURN, resData));
    }
}