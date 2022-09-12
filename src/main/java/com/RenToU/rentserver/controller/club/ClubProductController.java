package com.RenToU.rentserver.controller.club;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.RenToU.rentserver.dto.request.UpdateProductInfoDto;
import com.RenToU.rentserver.dto.response.preview.ProductPreviewDto;
import com.RenToU.rentserver.dto.service.UpdateProductInfoServiceDto;
import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.ProductService;
import com.RenToU.rentserver.application.S3Service;
import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.AddItemDto;
import com.RenToU.rentserver.dto.request.CreateProductDto;
import com.RenToU.rentserver.dto.response.ProductInfoDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.service.AddItemServiceDto;
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
    public ResponseEntity<?> createProduct(@PathVariable Long clubId,
            @Valid @ModelAttribute CreateProductDto createProductDto) {
        Long memberId = memberService.getMyIdWithAuthorities();
        MultipartFile image = createProductDto.getImage();
        String imagePath = null;
        // TODO registerProduct에서 사진 처리를하거나, 여기서 유저의 권한 체크 필요
        // 안하면 사진은 업로드되는데 데이터베이스에는 등록되지 않는 현상 발생
        if (image != null && !image.isEmpty()) {
            try {
                imagePath = s3Service.upload(image);
            } catch (IOException e) {
                throw new CustomException(ClubErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }
        CreateProductServiceDto productServiceDto = mapper.map(createProductDto, CreateProductServiceDto.class);
        Location location = new Location(createProductDto.getLocationName(), createProductDto.getLatitude(),
                createProductDto.getLongitude());
        productServiceDto.setLocation(location);
        productServiceDto.setImagePath(imagePath);
        productServiceDto.setClubId(clubId);
        productServiceDto.setMemberId(memberId);
        Product product = productService.registerProduct(productServiceDto);
        ProductInfoDto resData = ProductInfoDto.from(product);

        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.CREATE_PRODUCT, resData));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Long clubId, @PathVariable Long productId) {
        Long memberId = memberService.getMyIdWithAuthorities();
        Product product = productService.getProductById(productId);
        ProductInfoDto resData = ProductInfoDto.from(product, memberId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_PRODUCT_SUCCESS, resData));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProductInfo(@PathVariable Long clubId, @PathVariable Long productId,
            @Valid @ModelAttribute UpdateProductInfoDto updateProductInfoDto) {
        Long memberId = memberService.getMyIdWithAuthorities();
        MultipartFile image = updateProductInfoDto.getImage();
        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            try {
                imagePath = s3Service.upload(image);
            } catch (IOException e) {
                throw new CustomException(ClubErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }
        UpdateProductInfoServiceDto productServiceDto = mapper.map(updateProductInfoDto,
                UpdateProductInfoServiceDto.class);
        Location location = new Location(updateProductInfoDto.getLocationName(),
                updateProductInfoDto.getLatitude(),
                updateProductInfoDto.getLongitude());
        productServiceDto.setLocation(location);
        productServiceDto.setImagePath(imagePath);
        productServiceDto.setClubId(clubId);
        productServiceDto.setMemberId(memberId);
        Product product = productService.updateProductInfo(productId, productServiceDto);
        ProductInfoDto resData = ProductInfoDto.from(product);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK,
                ResponseMessage.UPDATE_PRODUCT_INFO, resData));
    }

    @DeleteMapping("/{productId}/items/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long clubId, @PathVariable Long productId,
            @PathVariable Long itemId) {
        Long memberId = memberService.getMyIdWithAuthorities();
        productService.deleteItem(memberId, clubId, productId, itemId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.DELETE_ITEM));
    }

    // TODO POST /productid/items -> itemDto(rentlapolicy, numbering) 받아서 create하기
    @PostMapping("/{productId}/items")
    public ResponseEntity<?> addItem(@PathVariable Long clubId, @PathVariable Long productId,
            @RequestBody AddItemDto dto) {
        Long memberId = memberService.getMyIdWithAuthorities();
        AddItemServiceDto serviceDto = mapper.map(dto, AddItemServiceDto.class);
        productService.addItem(memberId, clubId, productId, serviceDto);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.ADD_ITEM));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long clubId, @PathVariable Long productId) {
        Long memberId = memberService.getMyIdWithAuthorities();
        productService.deleteProduct(memberId, clubId, productId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK,
                ResponseMessage.DELETE_CLUB_PRODUCT, null));
    }

    @GetMapping("/search/all")
    public ResponseEntity<?> searchClubProductsAll(@PathVariable Long clubId) {
        Long memberId = memberService.getMyIdWithAuthorities();
        List<Product> products = productService.getProductsByClub(memberId, clubId);
        List<ProductPreviewDto> resData = products.stream()
                .map(n -> ProductPreviewDto.from(n))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_PRODUCT_SUCCESS, resData));
    }
}