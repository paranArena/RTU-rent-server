package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;

import java.util.List;

public interface ClubService {
        public List<Club> findClubs();

        public Club findClubById(long clubId);

        public Club findClubByName(String clubName);

        public void grantAdmin(Long clubId, Long ownerId, Long userId);

        public void grantUser(Long clubId, Long ownerId, Long userId);

        public ClubRole getMyRole(long memberId, long clubId);

        public ClubMember getClubMember(long memberId, long clubId, long clubMemberId);

        public List<Club> getMyClubs(long memberId);

        public Club createClub(Long memberId, String clubName, String clubIntro, String thumbnailPath,
                        List<String> clubHashtags);

        public void deleteClub(long memberId, long clubId);

        public List<ClubMember> getAllMembers(long clubId);

        public void requestClubJoin(Long clubId, Long memberId);

        public void cancelClubJoin(Long clubId, Long memberId);

        public List<ClubMember> searchClubJoinsAll(Long clubId, Long memberId);

        public void rejectClubJoin(Long clubId, Long ownerId, Long joinMemberId);

        public void acceptClubJoin(Long clubId, Long ownerId, Long joinMemberId);

        public void leaveClub(Long clubId, Long userId);

        public void removeClubMember(Long clubId, Long ownerId, Long memberId);

        List<Club> getMyClubRequests(long memberId);

        Club updateClubInfo(long memberId, long clubId, String name, String intro, String thumbnailPath,
                        List<String> hashtags);
}
