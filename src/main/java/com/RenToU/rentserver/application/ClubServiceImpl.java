package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Hashtag;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.exceptions.CannotJoinClubException;
import com.RenToU.rentserver.exceptions.ClubNotFoundException;
import com.RenToU.rentserver.exceptions.DuplicateMemberException;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.HashtagRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ClubServiceImpl implements ClubService{
    private final Mapper mapper;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final HashtagRepository hashtagRepository;

    private final ProductRepository productRepository;

    @Override
    public List<Club> findClubs() {
        return clubRepository.findAll();
    }

    @Override
    @Transactional
    public Club createClub(Long memberId, String clubName, String clubIntro, String thumbnailPath, List<String> hashtagNames ) {
        if (clubRepository.findByName(clubName).orElse(null) != null) {
            //TODO DuplicateClubException으로 교체?
            throw new DuplicateMemberException("이미 존재하는 모임 이름입니다.");
        }
        //회원 조회
        Member member = findMember(memberId);
        List<Hashtag> clubHashtags = hashtagNames.stream().map(hashtagName ->{
            return findHashtagByNameOrCreate(hashtagName);
        }).collect(Collectors.toList());
        //클럽 생성
        Club club = Club.createClub(clubName, clubIntro, thumbnailPath, member, clubHashtags);
        clubRepository.save(club);
        return club;
    }

    @Override
    @Transactional
    public void requestClubJoin(Long clubId, Long memberId) {
        //회원 조회
        Member member = findMember(memberId);
        Club club = findClub(clubId);
        validateCanJoin(club, member);
        ClubMember.createClubMember(club, member, ClubRole.WAIT);
        clubRepository.save(club);
    }
    @Override
    @Transactional
    public void acceptClubJoin(Long clubId, Long ownerId,Long joinMemberId){
        Club club = findClub(clubId);
        Member owner = findMember(ownerId);
        //요청자가 가입 허락 권한이 있는지 확인
        club.findClubMemberByMember(owner).validateAdmin();
        //가입자가 가입 신청 대기 상태인지 확인
        Member joiner = findMember(joinMemberId);
        ClubMember clubMember = club.findClubMemberByMember(joiner);
        //가입
        clubMember.acceptJoin();
        clubRepository.save(club);
    }
    @Override
    public Club findClubByName(String clubName){
        return clubRepository.findByName(clubName)
                .orElseThrow(() -> new ClubNotFoundException(clubName));

    }


    /**
     * validation
     */
    private void validateCanJoin(Club club,Member member) {
        if(club.getMemberList().contains(member)){
            throw new CannotJoinClubException(club.getId(),club.getName());
        }
    }

    private Member findMember(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }
    private Club findClub(Long id){
        return clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException(id));
    }
    private Hashtag findHashtagByNameOrCreate(String hashtagName){
        return hashtagRepository.findByName(hashtagName)
                .orElse(Hashtag.createHashtag(hashtagName));
    }

}
