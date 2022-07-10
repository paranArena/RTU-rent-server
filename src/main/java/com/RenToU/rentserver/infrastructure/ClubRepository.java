package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Club;

import java.util.List;
import java.util.Optional;

public interface ClubRepository {
    List<Club> findAll();

    Club findById(Long id);

    Club save(Club club);

    void delete(Club club);
}
