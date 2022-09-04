package com.RenToU.rentserver.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public String upload(MultipartFile file)  throws IOException {
        String fileName = file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), null));

        return s3Client.getUrl(bucket, fileName).toString();
    }

    public List<String> imageToPath(List<MultipartFile> images){
        List<MultipartFile> filteredImages = new ArrayList<>();
        List<String> imagePaths = new ArrayList<>();
        if(!isNull(images)) {
            filteredImages = images.stream().filter((img) -> !img.isEmpty()).collect(Collectors.toList());
        }
        if(!filteredImages.isEmpty()){
            imagePaths.addAll(
                    filteredImages.stream().map((img)->{
                        try {
                            return upload(img);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList())
            );
        }else{
            imagePaths.add("");
        }
        return imagePaths;
    }
}

