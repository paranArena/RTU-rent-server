package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<Member> findOneWithAuthoritiesByEmail(String email);

    @EntityGraph(attributePaths = "authorities")
    Optional<Member> findOneWithAuthoritiesByPhoneNumber(String phoneNumber);

    @EntityGraph(attributePaths = "authorities")
    Optional<Member> findOneWithAuthoritiesByStudentId(String studentId);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByStudentId(String studentId);
}
