package com.RenToU.rentserver.infrastructure.jpa;

import com.RenToU.rentserver.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(String hashtagName);
}
