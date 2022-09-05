package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalStatus;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final RentalRepository rentalRepository;

    @Transactional
    public void checkExpiredRentalWait() {
        List<Rental> rentals = rentalRepository.findAllByRentalStatus(RentalStatus.WAIT);
        rentals.stream().forEach(rental -> {
            if (rental.getRentDate().plusMinutes(10).isBefore(LocalDateTime.now())) {
                rentalRepository.deleteById(rental.getId());
            }
        });
    }
}
