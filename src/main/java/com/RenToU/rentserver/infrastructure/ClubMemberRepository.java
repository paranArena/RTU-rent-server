package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

}
