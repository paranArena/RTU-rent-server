package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;

import java.util.List;

public interface ClubService {
    public Club findClub(Long clubId);

    public List<Club> findClubs();

    public Long createClub(Long memberId, String clubName, String clubIntro);

    public void requestClubJoin(Long clubId, Long memberId);
    public void acceptClubJoin(Long clubId, Long ownerId, Long joinMemberId);
}
