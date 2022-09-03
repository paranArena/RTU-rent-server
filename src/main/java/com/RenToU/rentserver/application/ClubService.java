package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;

import java.util.List;

public interface ClubService {
    public List<Club> findClubs();
    public Club findClubById(long clubId);
    public Club findClubByName(String clubName);
    public void grantAdmin(Long clubId, Long ownerId,Long userId);
    public ClubRole getMyRole(long memberId, long clubId);
    public List<Club> getMyClubs(long memberId);
    public void leaveClub(Long clubId, Long userId);
    public Club createClub(Long memberId, String clubName, String clubIntro, String thumbnailPath, List<String> clubHashtags);
    public void deleteClub(long memberId, long clubId);
    public List<ClubMember> getAllMembers(long clubId);
    public void requestClubJoin(Long clubId, Long memberId);
    public void cancelClubJoin(Long clubId, Long memberId);
    public List<ClubMember> searchClubJoinsAll(Long clubId, Long memberId);
    public void acceptClubJoin(Long clubId, Long ownerId, Long joinMemberId);

    List<Club> getMyClubRequests(long memberId);
}
