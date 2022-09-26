package com.RenToU.rentserver.infrastructure.jpa;

import com.RenToU.rentserver.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
