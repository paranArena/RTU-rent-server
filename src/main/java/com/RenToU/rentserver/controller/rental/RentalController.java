package com.RenToU.rentserver.controller.rental;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.RenToU.rentserver.application.RentalService;
import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalHistory;
import com.RenToU.rentserver.dto.response.ItemDto;

import com.RenToU.rentserver.dto.response.preview.RentalHistoryPreviewDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.request.TmpMemberDto;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.response.preview.AdminRentalPreviewDto;
import com.RenToU.rentserver.exceptions.CustomException;
import com.RenToU.rentserver.exceptions.MemberErrorCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/rentals")
public class RentalController {

    private final MemberService memberService;
    private final RentalService rentalService;

    @PostMapping("/{itemId}/request")
    public ResponseEntity<?> requestRental(@PathVariable Long clubId, @PathVariable Long itemId) {
        long memberId = memberService.getMyIdWithAuthorities();
        Rental rental = rentalService.requestRental(memberId, itemId);
        ItemDto resData = ItemDto.from(rental.getItem());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_REQUEST_SUCCESS, resData));
    }

    @PutMapping("/{itemId}/apply")
    public ResponseEntity<?> applyRental(@PathVariable Long clubId, @PathVariable Long itemId) {
        Long memberId = memberService.getMyIdWithAuthorities();
        rentalService.applyRental(memberId, itemId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_APPLY_SUCCESS));
    }

    @PostMapping("/{itemId}/apply/admin")
    public ResponseEntity<?> justRental(@PathVariable Long clubId, @PathVariable Long itemId,
            @Valid @RequestBody TmpMemberDto dto) {
        Long clubAdminId = memberService.getMyIdWithAuthorities();
        rentalService.justRental(clubAdminId, clubId, itemId, dto.getName(), dto.getStudentId());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_APPLY_SUCCESS));
    }

    @PutMapping("/{itemId}/return/admin")
    public ResponseEntity<?> returnRentalAdmin(@PathVariable Long clubId, @PathVariable Long itemId,
            @Valid @RequestBody TmpMemberDto dto) {
        long clubAdminId = memberService.getMyIdWithAuthorities();
        Member member = memberService.findByStudentId(dto.getStudentId());
        if (!member.getName().equals(dto.getName())) {
            throw new CustomException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        rentalService.returnRentalAdmin(clubAdminId, clubId, itemId, member.getId());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_RETURN_SUCCESS));
    }

    @PutMapping("/{itemId}/return")
    public ResponseEntity<?> returnRental(@PathVariable Long clubId, @PathVariable Long itemId) {
        long memberId = memberService.getMyIdWithAuthorities();
        rentalService.returnRental(memberId, itemId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_RETURN_SUCCESS));

    }

    @DeleteMapping("/{itemId}/cancel")
    public ResponseEntity<?> cancelRental(@PathVariable Long clubId, @PathVariable Long itemId) {
        long memberId = memberService.getMyIdWithAuthorities();
        rentalService.cancelRental(memberId, itemId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_CANCEL_SUCCESS));
    }

    @GetMapping("/search/all")
    public ResponseEntity<?> searchClubRentalsAll(@PathVariable long clubId) {
        Long memberId = memberService.getMyIdWithAuthorities();
        List<Item> items = rentalService.getRentalsByClub(clubId, memberId);
        List<AdminRentalPreviewDto> resData = items.stream()
                .map((item) -> AdminRentalPreviewDto.from(item))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_RENTALS, resData));
    }

    @GetMapping("/history/search/all")
    public ResponseEntity<?> searchClubRentalHistoryAll(@PathVariable long clubId) {
        Long memberId = memberService.getMyIdWithAuthorities();
        List<RentalHistory> histories = rentalService.getRentalHistoryByClub(clubId, memberId);
        List<RentalHistoryPreviewDto> resData = histories.stream()
                .map((history) -> RentalHistoryPreviewDto.from(history))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.SEARCH_CLUB_RETURN, resData));
    }

    @DeleteMapping("/{itemId}/cancel/admin")
    public ResponseEntity<?> cancelRentalAdmin(@PathVariable Long clubId, @PathVariable Long itemId) {
        long memberId = memberService.getMyIdWithAuthorities();
        rentalService.cancelRentalAdmin(memberId, clubId, itemId);
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_CANCEL_SUCCESS));
    }
}