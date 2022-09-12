package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Club;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByName(String clubName);

    List<Club> findByNameContains(String clubName);
}
