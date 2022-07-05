package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class JPAMemberRepsitory implements MemberRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.of(em.find(Member.class,id));
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
