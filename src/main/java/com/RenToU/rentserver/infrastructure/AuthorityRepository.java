package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Authority;
import com.RenToU.rentserver.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByAuthorityName(String authorityNames);
}
