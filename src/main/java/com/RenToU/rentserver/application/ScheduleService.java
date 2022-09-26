package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalHistory;
import com.RenToU.rentserver.domain.RentalStatus;
import com.RenToU.rentserver.infrastructure.jpa.RentalHistoryRepository;
import com.RenToU.rentserver.infrastructure.jpa.RentalRepository;
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
    private final RentalHistoryRepository rentalHistoryRepository;

    @Transactional
    public void checkExpiredRentalWait() {
        List<Rental> rentals = rentalRepository.findAllByRentalStatus(RentalStatus.WAIT);
        if (rentals != null) {
            rentals.stream().forEach(rental -> {
                if (rental.getRentDate().plusMinutes(10).isBefore(LocalDateTime.now())) {
                    rental.validateWait();
                    rental.cancel();
                    RentalHistory rentalHistory = RentalHistory.RentalToHistory(rental);
                    rentalHistoryRepository.save(rentalHistory);
                    rentalRepository.deleteById(rental.getId());
                }
            });
        }
    }
}
