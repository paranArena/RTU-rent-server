package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;

import java.util.List;

public interface ClubService {
    public List<Club> findClubs();
    public Club findClubById(long clubId);
    public Club findClubByName(String clubName);

    public Club createClub(Long memberId, String clubName, String clubIntro, String thumbnailPath, List<String> clubHashtags);
    public void deleteClub(long memberId, long clubId);
    
    public void requestClubJoin(Long clubId, Long memberId);
    public void acceptClubJoin(Long clubId, Long ownerId, Long joinMemberId);

}
