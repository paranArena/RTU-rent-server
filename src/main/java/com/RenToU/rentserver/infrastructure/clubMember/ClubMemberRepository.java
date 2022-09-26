package com.RenToU.rentserver.infrastructure.clubMember;

import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.infrastructure.clubMember.ClubMemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends ClubMemberRepositoryCustom,JpaRepository<ClubMember, Long>  {

}
