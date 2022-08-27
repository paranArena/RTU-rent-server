package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
