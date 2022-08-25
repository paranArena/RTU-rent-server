package com.RenToU.rentserver.controller.club;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.RenToU.rentserver.application.ClubServiceImpl;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.S3Service;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.CreateProductDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.service.ProductServiceDto;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/products")
public class ProductController {
    
    private final MemberService memberService;
    private final ClubServiceImpl clubService;
    private final S3Service s3Service;
    private final Mapper mapper;

    @PostMapping("")
    public ResponseEntity<?> createProduct(@PathVariable Long clubId, @Valid @ModelAttribute CreateProductDto createProductDto) throws IOException{
        long memberId = memberService.getMyIdWithAuthorities();
        MultipartFile image = createProductDto.getImage();
        String imagePath = null;
        if(!image.isEmpty()){
            imagePath = s3Service.upload(image);
        }
        ProductServiceDto productServiceDto = mapper.map(createProductDto, ProductServiceDto.class);
        productServiceDto.setImagePath(imagePath);
        productServiceDto.setClubId(clubId);
        productServiceDto.setMemberId(memberId);

        clubService.registerProduct(productServiceDto);
        return new ResponseEntity<>(ResponseDto.res(StatusCode.OK, ResponseMessage.UPDATE_CLUB), HttpStatus.OK);
    }
}