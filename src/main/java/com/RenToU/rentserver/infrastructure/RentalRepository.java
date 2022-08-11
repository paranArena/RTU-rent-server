package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
