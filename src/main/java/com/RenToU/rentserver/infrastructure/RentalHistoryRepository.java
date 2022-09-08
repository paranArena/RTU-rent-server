package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.RentalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {
}
