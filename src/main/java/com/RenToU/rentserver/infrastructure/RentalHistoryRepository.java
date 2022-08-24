package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {
}
