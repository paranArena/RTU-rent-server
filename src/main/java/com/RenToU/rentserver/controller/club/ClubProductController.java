package com.RenToU.rentserver.controller.club;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.dto.response.preview.NotificationPreviewDto;
import com.RenToU.rentserver.dto.response.preview.ProductPreviewDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.ProductService;
import com.RenToU.rentserver.application.S3Service;
import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.CreateProductDto;
import com.RenToU.rentserver.dto.response.ProductDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.service.CreateProductServiceDto;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/products")
public class ClubProductController {
    
    private final MemberService memberService;
    private final ProductService productService;
    private final S3Service s3Service;
    private final Mapper mapper;

    @PostMapping("")
    public ResponseEntity<?> createProduct (@PathVariable Long clubId, @Valid @ModelAttribute CreateProductDto createProductDto) throws IOException{
        long memberId = memberService.getMyIdWithAuthorities();
        MultipartFile image = createProductDto.getImage();
        String imagePath = null;
        if(!image.isEmpty()){
            imagePath = s3Service.upload(image);
        }
        CreateProductServiceDto productServiceDto = mapper.map(createProductDto, CreateProductServiceDto.class);
        Location location = new Location(createProductDto.getLocationName(), createProductDto.getLatitude(), createProductDto.getLongitude());
        productServiceDto.setLocation(location);
        productServiceDto.setImagePath(imagePath);
        productServiceDto.setClubId(clubId);
        productServiceDto.setMemberId(memberId);
        Product product = productService.registerProduct(productServiceDto);
        ProductDto resData = ProductDto.from(product);

        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_PRODUCT, resData));
    }
    @GetMapping("/search/all")
    public ResponseEntity<?> searchProductByClub(@PathVariable long clubId){
        long memberId = memberService.getMyIdWithAuthorities();
        List<Product> products = productService.getProductsByClub(memberId, clubId);
        List<ProductPreviewDto> resData =
                products.stream()
                        .map(n-> ProductPreviewDto.from(n))
                        .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_NOTIFICATION_SUCCESS, resData));
    }
}