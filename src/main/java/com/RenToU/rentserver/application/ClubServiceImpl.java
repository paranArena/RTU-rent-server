package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.exceptions.CannotJoinClubException;
import com.RenToU.rentserver.infrastructure.JPAClubRepository;
import com.RenToU.rentserver.infrastructure.JPAMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ClubServiceImpl implements ClubService{

    private final JPAClubRepository clubRepository;
    private final JPAMemberRepository memberRepository;

    @Override
    public Club findClub(Long clubId) {
        return clubRepository.findById(clubId);
    }

    @Override
    public List<Club> findClubs() {
        return clubRepository.findAll();
    }

    @Override
    @Transactional
    public Long createClub(Long memberId, String clubName, String clubIntro) {
        //회원 조회
        Member member = memberRepository.findById(memberId);
        //클럽 생성
        Club club = Club.createClub(clubName, clubIntro,member);
        clubRepository.save(club);
        return club.getId();
    }

    @Override
    @Transactional
    public void requestClubJoin(Long clubId, Long memberId) {
        //회원 조회
        Member member = memberRepository.findById(memberId);
        Club club = clubRepository.findById(clubId);
        validateCanJoin(club, member);
        ClubMember clubmember = ClubMember.createClubMember(member, ClubRole.WAIT);
        club.addClubMember(clubmember);
        clubRepository.save(club);
    }
    @Override
    @Transactional
    public void acceptClubJoin(Long clubId, Long ownerId,Long joinMemberId){
        Club club = clubRepository.findById(clubId);
        Member owner = memberRepository.findById(ownerId);
        //요청자가 가입 허락 권한이 있는지 확인
        club.findClubMemberByMember(owner).validateAdmin();
        //가입자가 가입 신청 대기 상태인지 확인
        Member joiner = memberRepository.findById(joinMemberId);
        ClubMember clubMember = club.findClubMemberByMember(joiner);
        //가입
        clubMember.acceptJoin();
        clubRepository.save(club);
    }

    /**
     * 유저가 가입 신청 가능한 상태인지 확인
     * @param club 가입하려는 클럽
     * @param member 가입하려는 유저
     */
    private void validateCanJoin(Club club,Member member) {
        if(club.getMemberList().contains(member)){
            throw new CannotJoinClubException(club.getId(),club.getName());
        }
    }

}
