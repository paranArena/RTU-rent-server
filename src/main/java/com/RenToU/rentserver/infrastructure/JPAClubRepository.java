package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.exceptions.ClubNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class JPAClubRepository implements ClubRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Club> findAll() {
        return em.createQuery("select c from Club c", Club.class)
                .getResultList();
    }

    @Override
    public Club findById(Long id) {
        return Optional.of(em.find(Club.class,id))
                .orElseThrow(() -> new ClubNotFoundException(id));
    }

    @Override
    public Club save(Club club) {
        em.persist(club);
        return club;
    }

    @Override
    public void delete(Club club) {
        em.remove(club);
    }
}
