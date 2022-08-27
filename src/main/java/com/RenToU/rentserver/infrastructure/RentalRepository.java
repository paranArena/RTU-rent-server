package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Optional<Rental> findByItem(Item item);
}
