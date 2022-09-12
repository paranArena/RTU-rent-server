package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
