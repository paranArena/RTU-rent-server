package com.RenToU.rentserver.infrastructure.clubMember;

import com.RenToU.rentserver.domain.ClubMember;

import java.util.List;

public interface ClubMemberRepositoryCustom {
    List<ClubMember> searchByMemberIdWithClub(long memberId);
}
