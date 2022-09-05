package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Hashtag;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.exceptions.club.CannotJoinClubException;
import com.RenToU.rentserver.exceptions.club.ClubNotFoundException;
import com.RenToU.rentserver.exceptions.DuplicateMemberException;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.exceptions.clubMember.ClubMemberNotFoundException;
import com.RenToU.rentserver.infrastructure.ClubMemberRepository;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.HashtagRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ClubServiceImpl implements ClubService {
    private final Mapper mapper;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final HashtagRepository hashtagRepository;

    private final ClubMemberRepository clubMemberRepository;

    @Override
    public List<Club> findClubs() {
        return clubRepository.findAll();
    }

    @Override
    @Transactional
    public Club createClub(Long memberId, String clubName, String clubIntro, String thumbnailPath,
            List<String> hashtagNames) {
        if (clubRepository.findByName(clubName).orElse(null) != null) {
            // TODO DuplicateClubException으로 교체?
            throw new DuplicateMemberException("이미 존재하는 모임 이름입니다.");
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
        Member member = findMember(memberId);
        club.findClubMemberByMember(member).validateAdmin();
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
        Member member = findMember(memberId);
        Club club = findClub(clubId);
        club.findClubMemberByMember(member).validateAdmin();
        return club.getMemberList().stream()
                .filter((clubMember) -> clubMember.getRole() == ClubRole.WAIT)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void acceptClubJoin(Long clubId, Long ownerId, Long joinMemberId) {
        Club club = findClub(clubId);
        Member owner = findMember(ownerId);
        // 요청자가 가입 허락 권한이 있는지 확인
        club.findClubMemberByMember(owner).validateAdmin();
        // 가입자가 가입 신청 대기 상태인지 확인
        Member joiner = findMember(joinMemberId);
        ClubMember clubMember = club.findClubMemberByMember(joiner);
        // 가입
        clubMember.acceptJoin();
        clubRepository.save(club);
    }

    @Override
    @Transactional
    public void cancelClubJoin(Long clubId, Long memberId) {
        Club club = findClub(clubId);
        // 가입자가 가입 신청 대기 상태인지 확인
        Member joiner = findMember(memberId);
        ClubMember clubMember = club.findClubMemberByMember(joiner);
        // 가입
        clubMember.validateWait();
        clubMember.delete();
        clubMemberRepository.deleteById(clubMember.getId());
        clubRepository.save(club);
    }

    @Override
    public Club findClubByName(String clubName) {
        return clubRepository.findByName(clubName)
                .orElseThrow(() -> new ClubNotFoundException(clubName));
    }

    @Override
    public Club findClubById(long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new ClubNotFoundException(clubId));
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
        Member member = findMember(memberId);
        try {
            ClubMember cm = club.findClubMemberByMember(member);
            return cm.getRole();
        } catch (ClubMemberNotFoundException e) {
            return ClubRole.NONE;
        }
    }

    @Override
    @Transactional
    public void grantAdmin(Long clubId, Long ownerId, Long userId) {
        Club club = findClub(clubId);
        Member owner = findMember(ownerId);
        // 요청자가 가입 허락 권한이 있는지 확인
        club.findClubMemberByMember(owner).validateOwner();
        // 가입자가 가입 신청 대기 상태인지 확인
        Member user = findMember(userId);
        ClubMember clubMember = club.findClubMemberByMember(user);
        // 가입
        clubMember.grantAdmin();
        clubRepository.save(club);
    }

    @Override
    @Transactional
    public void leaveClub(Long clubId, Long userId) {
        Club club = findClub(clubId);
        Member user = findMember(userId);
        ClubMember clubMember = club.findClubMemberByMember(user);
        // 탈퇴
        clubMember.delete();
        clubMemberRepository.deleteById(clubMember.getId());
        clubRepository.save(club);
    }

    /**
     * validation
     */
    private void validateCanJoin(Club club, Member member) {
        club.getMemberList().stream().forEach(cl -> {
            if (cl.getMember() == member) {
                throw new CannotJoinClubException(club.getId(), club.getName(), "멤버가 이미 가입하였거나, 가입 신청 상태입니다.");
            }
        });
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    private Club findClub(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException(id));
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
