package com.RenToU.rentserver.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;
import com.RenToU.rentserver.exceptions.ErrorCode;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${external.mode}")
    private String MODE;

    public String upload(MultipartFile file, String filePath) throws IOException {
        s3Client.putObject(new PutObjectRequest(bucket, filePath, file.getInputStream(), null));
        return s3Client.getUrl(bucket, filePath).toString();
    }

    public String upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), null));

        return s3Client.getUrl(bucket, fileName).toString();
    }

    public List<String> imageToPath(List<MultipartFile> images, String loc) {
        List<MultipartFile> filteredImages = new ArrayList<>();
        List<String> imagePaths = new ArrayList<>();
        if (!isNull(images)) {
            filteredImages = images.stream().filter((img) -> !img.isEmpty()).collect(Collectors.toList());
        }
        if (!filteredImages.isEmpty()) {
            imagePaths.addAll(
                    filteredImages.stream().map((img) -> {
                        try {
                            String randomImgFileName = RandomStringUtils.random(20, true, true) + ".png";
                            String filePath = MODE + loc + randomImgFileName;
                            return upload(img, filePath);
                        } catch (IOException e) {
                            throw new CustomException(ClubErrorCode.IMAGE_UPLOAD_FAILED);
                        }
                    }).collect(Collectors.toList()));
        } else {
            imagePaths.add("");
        }
        return imagePaths;
    }
}
