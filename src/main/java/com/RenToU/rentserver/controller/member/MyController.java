package com.RenToU.rentserver.controller.member;

import com.RenToU.rentserver.application.ProductService;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.ClubRoleDto;
import com.RenToU.rentserver.dto.response.MemberInfoDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.response.preview.ClubPreviewDto;
import com.RenToU.rentserver.dto.response.preview.NotificationPreviewDto;
import com.RenToU.rentserver.dto.response.preview.ProductPreviewDto;
import com.RenToU.rentserver.dto.response.preview.RentalPreviewDto;
import com.github.dozermapper.core.Mapper;
import com.RenToU.rentserver.application.ClubService;
import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.application.NotificationService;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.domain.Rental;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/my")
public class MyController {
    private final MemberService memberService;
    private final ClubService clubService;
    private final NotificationService notificationService;

    private final ProductService productService;
    private final Mapper mapper;

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        Member member = memberService.getMyUserWithAuthorities();
        MemberInfoDto resData = mapper.map(member, MemberInfoDto.class);

        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MY_INFO, resData));
    }

    @GetMapping("/clubs")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyClubs(HttpServletRequest request) {
        Long memberId = memberService.getMyIdWithAuthorities();
        List<Club> clubs = clubService.getMyClubs(memberId);
        List<ClubPreviewDto> resData = clubs.stream()
                .map((club) -> {
                    ClubPreviewDto dto = ClubPreviewDto.from(club);
                    dto.setClubRole(clubService.getMyRole(memberId, club.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MY_CLUB, resData));
    }

    @GetMapping("/clubs/role/wait")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyClubRequests() {
        Long memberId = memberService.getMyIdWithAuthorities();
        List<Club> clubs = clubService.getMyClubRequests(memberId);
        List<ClubPreviewDto> resData = clubs.stream()
                .map((club) -> {
                    ClubPreviewDto dto = ClubPreviewDto.from(club);
                    dto.setClubRole(clubService.getMyRole(memberId, club.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MY_CLUB_REQUESTS, resData));
    }

    @GetMapping("/notifications")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyNotifications(HttpServletRequest request) {
        Long memberId = memberService.getMyIdWithAuthorities();
        List<Notification> notifications = notificationService.getMyNotifications(memberId);
        List<NotificationPreviewDto> resData = notifications.stream()
                .map((n) -> NotificationPreviewDto.from(n))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MY_NOTIFICATION, resData));
    }

    @GetMapping("/clubs/{clubId}/role")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyClubRole(@PathVariable Long clubId) {
        Long memberId = memberService.getMyIdWithAuthorities();
        ClubRole clubRole = clubService.getMyRole(memberId, clubId);
        ClubRoleDto resData = new ClubRoleDto(clubRole);

        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MY_CLUB_ROLE, resData));
    }

    @GetMapping("/products")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyProducts(HttpServletRequest request) {
        Long memberId = memberService.getMyIdWithAuthorities();
        List<Product> products = productService.getMyProducts(memberId);
        List<ProductPreviewDto> resData = products.stream()
                .map(product -> ProductPreviewDto.from(product))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MY_PRODUCT, resData));
    }

    @GetMapping("/rentals")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getMyRentals(HttpServletRequest request) {
        List<Item> items = memberService.getMyUserWithAuthorities().getRentals().stream()
                .map((rental) -> rental.getItem())
                .collect(Collectors.toList());
        List<RentalPreviewDto> resData = items.stream()
                .map((item) -> RentalPreviewDto.from(item))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_MY_RENT, resData));
    }

    @GetMapping("/quit")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> quitService(HttpServletRequest request) {
        Long memberId = memberService.getMyIdWithAuthorities();
        memberService.deleteMember(memberId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.QUIT_SUCCESS));
    }
}
