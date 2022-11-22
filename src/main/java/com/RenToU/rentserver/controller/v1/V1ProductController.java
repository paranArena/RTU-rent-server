package com.RenToU.rentserver.controller.v1;

import javax.validation.Valid;

import com.RenToU.rentserver.dto.request.V1CreateProductDto;
import com.RenToU.rentserver.dto.request.V1UpdateProductInfoDto;
import com.RenToU.rentserver.dto.service.V1CreateProductServiceDto;
import com.RenToU.rentserver.dto.service.V1UpdateProductInfoServiceDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.ProductService;
import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.ProductInfoDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/clubs/{clubId}/products")
public class V1ProductController {

    private final MemberService memberService;
    private final ProductService productService;
    private final Mapper mapper;
    @Value("${external.mode}")
    private String MODE;

    @PostMapping("")
    public ResponseEntity<?> createProduct(@PathVariable Long clubId,
            @Valid @RequestBody V1CreateProductDto createProductDto) {
        Long memberId = memberService.getMyIdWithAuthorities();

        V1CreateProductServiceDto productServiceDto = mapper.map(createProductDto, V1CreateProductServiceDto.class);
        Location location = new Location(createProductDto.getLocationName(), createProductDto.getLatitude(),
                createProductDto.getLongitude());
        productServiceDto.setLocation(location);
        // productServiceDto.setImagePath(imagePath);
        productServiceDto.setClubId(clubId);
        productServiceDto.setMemberId(memberId);
        Product product = productService.registerProduct(productServiceDto);

        ProductInfoDto resData = ProductInfoDto.from(product);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_PRODUCT, resData));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProductInfo(@PathVariable Long clubId, @PathVariable Long productId,
            @Valid @RequestBody V1UpdateProductInfoDto updateProductInfoDto) {
        Long memberId = memberService.getMyIdWithAuthorities();
        V1UpdateProductInfoServiceDto productServiceDto = mapper.map(updateProductInfoDto,
                V1UpdateProductInfoServiceDto.class);
        Location location = new Location(updateProductInfoDto.getLocationName(),
                updateProductInfoDto.getLatitude(),
                updateProductInfoDto.getLongitude());
        productServiceDto.setLocation(location);
        productServiceDto.setClubId(clubId);
        productServiceDto.setMemberId(memberId);
        Product product = productService.updateProductInfo(productId, productServiceDto);
        ProductInfoDto resData = ProductInfoDto.from(product);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK,
                ResponseMessage.UPDATE_PRODUCT_INFO, resData));
    }
}
