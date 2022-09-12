package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.ClubHashtag;
import com.RenToU.rentserver.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubHashtagRepository extends JpaRepository<ClubHashtag, Long> {
    List<ClubHashtag> findByHashtag(Hashtag Hashtag);
}
