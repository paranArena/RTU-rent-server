package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubHashtag;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Hashtag;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;
import com.RenToU.rentserver.exceptions.MemberErrorCode;
import com.RenToU.rentserver.infrastructure.ClubHashtagRepository;
import com.RenToU.rentserver.infrastructure.ClubMemberRepository;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.HashtagRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.RenToU.rentserver.domain.ClubRole.ADMIN;
import static com.RenToU.rentserver.domain.ClubRole.OWNER;
import static com.RenToU.rentserver.domain.ClubRole.WAIT;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final HashtagRepository hashtagRepository;

    private final ClubMemberRepository clubMemberRepository;

    private final ClubHashtagRepository clubHashtagRepository;

    @Override
    public List<Club> findClubs() {
        return clubRepository.findAll();
    }

    @Override
    @Transactional
    public Club createClub(Long memberId, String clubName, String clubIntro, String thumbnailPath,
            List<String> hashtagNames) {
        if (clubRepository.findByName(clubName).orElse(null) != null) {
            throw new CustomException(ClubErrorCode.DUP_CLUB_NAME);
        }
        // 회원 조회
        Member member = findMember(memberId);
        List<Hashtag> clubHashtags = hashtagNames.stream().map(hashtagName -> {
            return findHashtagByNameOrCreate(hashtagName);
        }).collect(Collectors.toList());
        // 클럽 생성
        Club club = Club.createClub(clubName, clubIntro, thumbnailPath, member, clubHashtags);
        clubRepository.save(club);
        return club;
    }

    @Override
    @Transactional
    public void deleteClub(long memberId, long clubId) {
        Club club = findClubById(clubId);
        club.findClubMemberByMemberId(memberId).validateRole(true,OWNER,ADMIN);
        clubRepository.deleteById(clubId);
    }

    @Override
    @Transactional
    public List<ClubMember> getAllMembers(long clubId) {
        Club club = findClubById(clubId);
        return getAllClubUser(club);
    }

    @Override
    @Transactional
    public void requestClubJoin(Long clubId, Long memberId) {
        // 회원 조회
        Member member = findMember(memberId);
        Club club = findClub(clubId);
        validateCanJoin(club, member);
        // 왠지 모르겠는데 print를 빼면 정상 작동을 안합니다... 추후에 해결해야 할 부분.
        member.getClubList().forEach(cm -> {
            System.out.println(cm.toString());
        });
        ClubMember clubMember = ClubMember.createClubMember(club, member, ClubRole.WAIT);
        clubMemberRepository.save(clubMember);
        System.out.println("size" + member.getClubList().size() + ",clubId " + club.getId());
    }

    @Override
    @Transactional
    public List<ClubMember> searchClubJoinsAll(Long clubId, Long memberId) {
        Club club = findClub(clubId);
        club.findClubMemberByMemberId(memberId).validateRole(true,OWNER,ADMIN);
        return club.getMemberList().stream()
                .filter((clubMember) -> clubMember.getRole() == ClubRole.WAIT)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void acceptClubJoin(Long clubId, Long ownerId, Long joinerId) {
        Club club = findClub(clubId);
        // 요청자가 가입 허락 권한이 있는지 확인
        club.findClubMemberByMemberId(ownerId).validateRole(true,OWNER,ADMIN);
        // 가입자가 가입 신청 대기 상태인지 확인
        ClubMember clubMember = club.findClubMemberByMemberId(joinerId);
        // 가입
        clubMember.acceptJoin();
        clubRepository.save(club);
    }

    @Override
    @Transactional
    public void cancelClubJoin(Long clubId, Long memberId) {
        Club club = findClub(clubId);
        // 가입자가 가입 신청 대기 상태인지 확인
        ClubMember clubMember = club.findClubMemberByMemberId(memberId);
        // 가입
        clubMember.validateRole(true,WAIT);
        clubMember.delete();
        clubMemberRepository.deleteById(clubMember.getId());
        clubRepository.save(club);
    }

    @Override
    @Transactional
    public void rejectClubJoin(Long clubId, Long ownerId, Long joinerId) {
        Club club = findClub(clubId);
        // 요청자가 가입 허락 권한이 있는지 확인
        club.findClubMemberByMemberId(ownerId).validateRole(true,OWNER,ADMIN);
        // 가입자가 가입 신청 대기 상태인지 확인
        ClubMember clubMember = club.findClubMemberByMemberId(joinerId);
        // 가입
        clubMember.validateRole(true,WAIT);
        clubMember.delete();
        clubMemberRepository.deleteById(clubMember.getId());
        clubRepository.save(club);
    }

    @Override
    public Club findClubByName(String clubName) {
        return clubRepository.findByName(clubName)
                .orElseThrow(() -> new CustomException(ClubErrorCode.CLUB_NOT_FOUND));
    }

    @Override
    public Club findClubById(long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new CustomException(ClubErrorCode.CLUB_NOT_FOUND));
    }

    @Override
    public List<Club> getMyClubRequests(long memberId) {
        Member member = findMember(memberId);
        List<ClubMember> requests = member.getClubList().stream().filter(cm -> cm.getRole().equals(ClubRole.WAIT))
                .collect(Collectors.toList());
        return requests.stream().map(cm -> cm.getClub()).collect(Collectors.toList());
    }

    @Override
    public List<Club> getMyClubs(long memberId) {
        Member member = findMember(memberId);
        List<ClubMember> requests = member.getClubList().stream().filter(cm -> !cm.getRole().equals(ClubRole.WAIT))
                .collect(Collectors.toList());
        return requests.stream().map(cm -> cm.getClub()).collect(Collectors.toList());
    }

    @Override
    public ClubRole getMyRole(long memberId, long clubId) {
        Club club = findClubById(clubId);
        try {
            ClubMember cm = club.findClubMemberByMemberId(memberId);
            return cm.getRole();
        } catch (CustomException e) {
            if (e.getErrorCode() == ClubErrorCode.CLUBMEMBER_NOT_FOUND)
                return ClubRole.NONE;
            else {
                throw e;
            }
        }
    }

    @Override
    public ClubMember getClubMember(long memberId, long clubId, long clubMemberId) {
        Club club = findClubById(clubId);
        club.findClubMemberByMemberId(memberId).validateRole(true,OWNER,ADMIN);
        ClubMember clubMember = club.findClubMemberByMemberId(clubMemberId);
        return clubMember;
    }

    @Override
    @Transactional
    public void grantAdmin(Long clubId, Long ownerId, Long userId) {
        Club club = findClub(clubId);
        // 요청자가 가입 허락 권한이 있는지 확인
        club.findClubMemberByMemberId(ownerId).validateRole(true, OWNER);
        // 가입자가 가입 신청 대기 상태인지 확인
        ClubMember clubMember = club.findClubMemberByMemberId(userId);
        // 가입
        clubMember.grantAdmin();
        clubRepository.save(club);
    }

    @Override
    @Transactional
    public void grantUser(Long clubId, Long ownerId, Long userId) {
        Club club = findClub(clubId);
        // 요청자가 가입 허락 권한이 있는지 확인
        club.findClubMemberByMemberId(ownerId).validateRole(true,OWNER);
        // 가입자가 가입 신청 대기 상태인지 확인
        ClubMember clubMember = club.findClubMemberByMemberId(userId);
        // 가입
        clubMember.grantUser();
        clubRepository.save(club);
    }

    @Override
    @Transactional
    public void leaveClub(Long clubId, Long userId) {
        Club club = findClub(clubId);
        ClubMember clubMember = club.findClubMemberByMemberId(userId);
        // 탈퇴
        clubMember.delete();
        clubMemberRepository.deleteById(clubMember.getId());
        clubRepository.save(club);
    }

    @Override
    @Transactional
    public void removeClubMember(Long clubId, Long ownerId, Long memberId) {
        Club club = findClub(clubId);
        club.findClubMemberByMemberId(ownerId).validateRole(true,OWNER);
        ClubMember clubMember = club.findClubMemberByMemberId(memberId);
        // 탈퇴
        clubMember.delete();
        clubMemberRepository.deleteById(clubMember.getId());
        clubRepository.save(club);
    }

    @Override
    @Transactional
    public Club updateClubInfo(long memberId, long clubId, String name, String intro, String thumbnailPath,
            List<String> hashtagNames) {
        Club club = findClub(clubId);
        club.findClubMemberByMemberId(memberId).validateRole(true,OWNER,ADMIN);
        List<Hashtag> clubHashtags = hashtagNames.stream().map(hashtagName -> {
            return findHashtagByNameOrCreate(hashtagName);
        }).collect(Collectors.toList());
        eraseBeforeClubHashtag(club);
        club.updateClub(name, intro, thumbnailPath, clubHashtags);
        hashtagRepository.saveAll(clubHashtags);
        clubRepository.save(club);
        return club;
    }

    private void eraseBeforeClubHashtag(Club club) {
        List<ClubHashtag> clubHashtags = club.getHashtags();
        clubHashtags.forEach(clubHashtag -> {
            club.deleteHashtag(clubHashtag);
            clubHashtagRepository.deleteById(clubHashtag.getId());
        });
    }

    /**
     * validation
     */
    private void validateCanJoin(Club club, Member member) {
        club.getMemberList().stream().forEach(cl -> {
            if (cl.getMember() == member) {
                throw new CustomException(ClubErrorCode.CANT_REQUEST_JOIN);
            }
        });
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Club findClub(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new CustomException(ClubErrorCode.CLUB_NOT_FOUND));
    }

    private Hashtag findHashtagByNameOrCreate(String hashtagName) {
        return hashtagRepository.findByName(hashtagName)
                .orElse(Hashtag.createHashtag(hashtagName));
    }

    private List<ClubMember> getAllClubUser(Club club) {
        return club.getMemberList().stream().filter(cm -> cm.getRole() != ClubRole.WAIT).collect(Collectors.toList());
        // return users.stream().map(cm->cm.getMember()).collect(Collectors.toList());
    }
}
