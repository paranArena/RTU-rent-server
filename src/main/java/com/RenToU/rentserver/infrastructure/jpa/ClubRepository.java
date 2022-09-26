package com.RenToU.rentserver.infrastructure.jpa;

import com.RenToU.rentserver.domain.Club;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClubRepository extends JpaRepository<Club, Long> {
    @Override
    @EntityGraph("club.ClubMember")
    Optional<Club>findById(Long id);
    Optional<Club> findByName(String clubName);

    List<Club> findByNameContains(String clubName);
}
