package com.RenToU.rentserver.application;

import com.RenToU.rentserver.DTO.NotificationDTO;
import com.RenToU.rentserver.DTO.ProductDTO;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Notification;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.exceptions.CannotJoinClubException;
import com.RenToU.rentserver.infrastructure.JPAClubRepository;
import com.RenToU.rentserver.infrastructure.JPAMemberRepository;
import com.RenToU.rentserver.infrastructure.JPAProductRepository;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ClubServiceImpl implements ClubService{
    private final Mapper mapper;
    private final JPAClubRepository clubRepository;

    private final JPAProductRepository productRepository;
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
     *Notification
     */
    @Override
    public Long createNotification(Long clubId, Long writerId, NotificationDTO notificationDTO) {
        Club club = clubRepository.findById(clubId);
        Member writer = memberRepository.findById(writerId);
        club.findClubMemberByMember(writer).validateAdmin();
        Notification notification = mapper.map(notificationDTO,Notification.class);
        club.addNotification(notification);
        clubRepository.save(club);
        return notification.getId();
    }
    /**
     * product
     */
    @Override
    public void registerProduct(Long clubId, ProductDTO productDTO, Long memberId){
        Club club = clubRepository.findById(clubId);
        Member requester = memberRepository.findById(memberId);
        club.findClubMemberByMember(requester).validateAdmin();
        Product product = mapper.map(productDTO,Product.class);
        club.addProduct(product);
        clubRepository.save(club);
    }
    @Override
    public void registerItem(Long productId, Long memberId){
        Product product = productRepository.findById(productId);
        Member requester = memberRepository.findById(memberId);
        Club club = product.getClub();
        club.findClubMemberByMember(requester).validateAdmin();
        product.addSeq();
        Item item = Item.createItem(product);
        product.addItem(item);
        productRepository.save(product);
    }

    /**
     * validation
     */
    private void validateCanJoin(Club club,Member member) {
        if(club.getMemberList().contains(member)){
            throw new CannotJoinClubException(club.getId(),club.getName());
        }
    }

}
