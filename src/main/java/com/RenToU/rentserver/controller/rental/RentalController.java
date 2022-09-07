package com.RenToU.rentserver.controller.rental;

import java.io.IOException;
import java.util.List;

import com.RenToU.rentserver.application.RentalService;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalHistory;
import com.RenToU.rentserver.dto.response.IdDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RenToU.rentserver.application.MemberService;
import com.RenToU.rentserver.dto.StatusCode;
import com.RenToU.rentserver.dto.response.ResponseDto;
import com.RenToU.rentserver.dto.response.ResponseMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/{clubId}/rentals")
public class RentalController {

    private final MemberService memberService;
    private final RentalService rentalService;

    @PostMapping("/{itemId}/request")
    public ResponseEntity<?> requestRental(@PathVariable Long clubId, @PathVariable Long itemId) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        Rental rental = rentalService.requestRental(memberId, itemId);
        IdDto resData = new IdDto("rentalId", rental.getId());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_REQUEST_SUCCESS, resData));
    }

    @PostMapping("/{rentId}/apply")
    public ResponseEntity<?> applyRental(@PathVariable Long clubId, @PathVariable Long rentId) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        Rental rental = rentalService.applyRental(memberId, rentId);
        IdDto resData = new IdDto("rentalId", rental.getId());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_APPLY_SUCCESS, resData));
    }

    @PostMapping("/{rentId}/return")
    public ResponseEntity<?> returnRental(@PathVariable Long clubId, @PathVariable Long rentId) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        RentalHistory rentalHistory = rentalService.returnRental(memberId, rentId);
        IdDto resData = new IdDto("rentalHistoryId", rentalHistory.getId());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_RETURN_SUCCESS, resData));
    }

    @PostMapping("/{rentId}/cancel")
    public ResponseEntity<?> cancelRental(@PathVariable Long clubId, @PathVariable Long rentId) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        RentalHistory rentalHistory = rentalService.cancelRental(memberId, rentId);
        IdDto resData = new IdDto("rentalHistoryId", rentalHistory.getId());
        return ResponseEntity.ok(ResponseDto.res(StatusCode.OK, ResponseMessage.RENT_CANCEL_SUCCESS, resData));
    }
    @GetMapping("/searh/all")
    public ResponseEntity<?> searchRentalByClub(@PathVariable Long clubId) throws IOException {
        long memberId = memberService.getMyIdWithAuthorities();
        List<Rental> rentals = rentalService.getRentalsByClub(clubId, memberId);
        return null;
    }
}