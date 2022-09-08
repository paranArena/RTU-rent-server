package com.RenToU.rentserver.dto.response.preview;

import com.RenToU.rentserver.domain.RentalHistory;
import com.RenToU.rentserver.domain.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalHistoryPreviewDto {
    private Long id;
    private String thumbnailPath;
    private String productName;
    private String memberName;
    private LocalDateTime rentDate;
    private LocalDateTime expDate;
    private LocalDateTime returnDate;
    private RentalStatus rentalStatus;
    private int numbering;


    public static RentalHistoryPreviewDto from(RentalHistory history) {
        if (history == null)
            return null;

        return RentalHistoryPreviewDto.builder()
                .id(history.getId())
                .productName(history.getItem().getProduct().getName())
                .thumbnailPath(history.getItem().getProduct().getImagePath())
                .memberName(history.getMember().getName())
                .rentDate(history.getRentDate())
                .expDate(history.getExpDate())
                .rentalStatus(history.getRentalStatus())
                .returnDate(history.getReturnDate())
                .numbering(history.getItem().getNumbering())
                .build();
    }
}
