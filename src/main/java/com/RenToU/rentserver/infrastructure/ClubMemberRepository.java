package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    List<ClubMember> findAllByClubId(Long clubId);

}
