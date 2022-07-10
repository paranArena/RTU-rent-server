package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class JPAMemberRepository implements MemberRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    @Override
    public Member findById(Long id) {
        return Optional.of(em.find(Member.class,id))
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public void delete(Member member) {
        em.remove(member);
    }

}
