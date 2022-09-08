package com.RenToU.rentserver.controller.rental;

import java.util.List;
import java.util.stream.Collectors;

import com.RenToU.rentserver.application.RentalService;
import com.RenToU.rentserver.domain.Item;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;
import com.RenToU.rentserver.dto.response.preview.AdminRentalPreviewDto;
import com.RenToU.rentserver.exceptions.CustomException;
import com.RenToU.rentserver.exceptions.RentalErrorCode;
import com.RenToU.rentserver.infrastructure.ItemRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/rentals")
public class RentalController {

    private final MemberService memberService;
    private final RentalService rentalService;
    private final ItemRepository itemRepository;

    @PostMapping("/{itemId}/request")
    public ResponseEntity<?> requestRental(@PathVariable Long clubId, @PathVariable Long itemId) {
        long memberId = memberService.getMyIdWithAuthorities();
        Rental rental = rentalService.requestRental(memberId, itemId);
        ItemDto resData = ItemDto.from(rental.getItem());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_REQUEST_SUCCESS, resData));
    }

    // TODO RentalNotFoundException 처리
    // CanNotRentException 처리

    @PutMapping("/{itemId}/apply")
    public ResponseEntity<?> applyRental(@PathVariable Long clubId, @PathVariable Long itemId) {
        Long memberId = memberService.getMyIdWithAuthorities();
        try {
            Long rentalId = itemRepository.getReferenceById(itemId).getRental().getId();
            rentalService.applyRental(memberId, rentalId);
            return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_APPLY_SUCCESS));
        } catch (NullPointerException e) {
            throw new CustomException(RentalErrorCode.RENTAL_NOT_FOUND);
        }
    }

    @PutMapping("/{itemId}/return")
    public ResponseEntity<?> returnRental(@PathVariable Long clubId, @PathVariable Long itemId) {
        long memberId = memberService.getMyIdWithAuthorities();
        try {
            Long rentalId = itemRepository.getReferenceById(itemId).getRental().getId();
            RentalHistory rentalHistory = rentalService.returnRental(memberId, rentalId);
            return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_RETURN_SUCCESS));
        } catch (NullPointerException e) {
            throw new CustomException(RentalErrorCode.RENTAL_NOT_FOUND);
        }
    }

    @DeleteMapping("/{itemId}/cancel")
    public ResponseEntity<?> cancelRental(@PathVariable Long clubId, @PathVariable Long itemId) {
        long memberId = memberService.getMyIdWithAuthorities();
        try {
            Long rentalId = itemRepository.getReferenceById(itemId).getRental().getId();
            RentalHistory rentalHistory = rentalService.cancelRental(memberId, rentalId);
            return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_CANCEL_SUCCESS));
        } catch (NullPointerException e) {
            throw new CustomException(RentalErrorCode.RENTAL_NOT_FOUND);
        }
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
}