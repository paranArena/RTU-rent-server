package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club,Long> {

}
