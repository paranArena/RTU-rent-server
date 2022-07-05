package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    List<Member> findAll();

    Optional<Member> findById(Long id);

    Member save(Member member);

    void delete(Member member);

}
