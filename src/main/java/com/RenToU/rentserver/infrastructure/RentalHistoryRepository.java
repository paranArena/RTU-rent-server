package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.RentalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {
    List<RentalHistory> findAllByItem(Item item);
}
