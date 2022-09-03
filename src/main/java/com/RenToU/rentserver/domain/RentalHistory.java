package com.RenToU.rentserver.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class RentalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_history_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="member_id")
    private Member member;
    @Enumerated(EnumType.STRING)
    private RentalStatus rentalStatus;
    private LocalDateTime rentDate;//렌탈 시작 시간
    private LocalDateTime expDate;//반납 기한
    private LocalDateTime returnDate;//렌탈 만료 시간
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "item_id")
    private Item item;
    public static RentalHistory RentalToHistory(Rental rental) {
        RentalHistory rentalHistory = new RentalHistory();
        rentalHistory.member = rental.getMember();
        rentalHistory.item = rental.getItem();
        rentalHistory.rentalStatus = rental.getRentalStatus();
        rentalHistory.rentDate = rental.getRentDate();
        rentalHistory.expDate = rental.getExpDate();
        rentalHistory.returnDate = LocalDateTime.now();
        return rentalHistory;
    }
}
