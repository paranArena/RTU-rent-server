package com.RenToU.rentserver.domain;

import com.RenToU.rentserver.exceptions.CannotRentException;
import com.RenToU.rentserver.exceptions.NotRentingException;
import com.RenToU.rentserver.exceptions.NotWaitingException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;

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

import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private RentalStatus rentalStatus;

    private LocalDateTime rentDate;// 렌탈 시작 시간

    private LocalDateTime expDate;// 렌탈 만료 시간

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private Item item;

    public static Rental createRental(Item item, Member member) {
        Rental rental = Rental.builder()
                .rentalStatus(RentalStatus.WAIT)
                .item(item)
                .rentDate(LocalDateTime.now())
                .build();
        member.addRental(rental);
        item.setRental(rental);
        return rental;
    }

    public void cancel() {
        if (this.rentalStatus != RentalStatus.WAIT) {
            throw new IllegalStateException("렌탈을 취소할 수 없는 상태입니다.");
        }
        this.rentalStatus = RentalStatus.CANCEL;
        this.getItem().finishRental();

    }

    public void startRental() {
        this.rentalStatus = RentalStatus.RENT;
        this.rentDate = LocalDateTime.now();
        this.setExpDate();
    }

    public void finishRental() {
        this.rentalStatus = RentalStatus.DONE;
        this.getItem().finishRental();
    }

    private void setExpDate() {
        if (this.getItem().getRentalPolicy() == RentalPolicy.FIFO) {
            this.expDate = LocalDateTime.now().plusDays(this.getItem().getProduct().getFifoRentalPeriod());
        } else {
            this.expDate = LocalDateTime.now().plusDays(this.getItem().getProduct().getReserveRentalPeriod());
        }
    }

    public void validateRent() {
        if (this.rentalStatus != RentalStatus.RENT) {
            throw new NotRentingException(this.id);
        }
    }

    public void validateWait() {
        if (this.rentalStatus != RentalStatus.WAIT) {
            throw new NotWaitingException(this.id);
        }
    }

    public void validateMember(Member member) {
        if (this.member != member) {
            throw new NotRentingException(this.id);
        }
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setRentDateBeforeTenM() {
        this.rentDate = this.rentDate.minusMinutes(10);
    }

    public void checkLate() {
        if (this.expDate.isBefore(LocalDateTime.now())) {
            this.rentalStatus = RentalStatus.LATE;
        }
    }

    public void deleteRental() {
        this.member.deleteRental(this);
        this.member = null;
        this.item.deleteRental();
        this.item = null;

    }
}
