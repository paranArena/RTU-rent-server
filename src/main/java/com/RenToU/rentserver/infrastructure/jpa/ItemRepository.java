package com.RenToU.rentserver.infrastructure.jpa;

import com.RenToU.rentserver.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
