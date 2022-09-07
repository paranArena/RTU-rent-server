package com.RenToU.rentserver.controller.rental;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.RenToU.rentserver.application.ClubService;
import com.RenToU.rentserver.application.RentalService;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalHistory;
import com.RenToU.rentserver.dto.response.IdDto;
import com.RenToU.rentserver.dto.response.ItemDto;

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
import com.RenToU.rentserver.dto.response.preview.RentalPreviewDto;
import com.RenToU.rentserver.exceptions.RentalNotFoundException;
import com.RenToU.rentserver.infrastructure.ItemRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/rentals")
public class RentalController {

    private final MemberService memberService;
    private final RentalService rentalService;
    private final ItemRepository itemRepository;

    private final ClubService clubService;

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
            throw new RentalNotFoundException();
        }
    }

    @PutMapping("/{itemId}/return")
    public ResponseEntity<?> returnRental(@PathVariable Long clubId, @PathVariable Long itemId) {
        long memberId = memberService.getMyIdWithAuthorities();
        try {
            Long rentalId = itemRepository.getReferenceById(itemId).getRental().getId();
            RentalHistory rentalHistory = rentalService.returnRental(memberId, rentalId);
            IdDto resData = new IdDto("rentalHistoryId", rentalHistory.getId());
            return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_RETURN_SUCCESS, resData));
        } catch (NullPointerException e) {
            throw new RentalNotFoundException();
        }
    }

    @DeleteMapping("/{itemId}/cancel")
    public ResponseEntity<?> cancelRental(@PathVariable Long clubId, @PathVariable Long itemId) {
        long memberId = memberService.getMyIdWithAuthorities();
        try {
            Long rentalId = itemRepository.getReferenceById(itemId).getRental().getId();
            RentalHistory rentalHistory = rentalService.cancelRental(memberId, rentalId);
            IdDto resData = new IdDto("rentalHistoryId", rentalHistory.getId());
            return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_CANCEL_SUCCESS, resData));
        } catch (NullPointerException e) {
            throw new RentalNotFoundException();
        }
    }

    @GetMapping("/search/all")
    public ResponseEntity<?> searchClubRentalsAll(@PathVariable long clubId) {
        Long memberId = memberService.getMyIdWithAuthorities();
        List<Item> items = rentalService.getRentalsByClub(clubId, memberId);
        List<AdminRentalPreviewDto> resData = items.stream()
                .map((item) -> AdminRentalPreviewDto.from(item))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.GET_CLUB, resData));
    }
}