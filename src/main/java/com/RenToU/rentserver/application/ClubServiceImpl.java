package com.RenToU.rentserver.application;

import com.RenToU.rentserver.DTO.ClubDTO;
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
import com.RenToU.rentserver.exceptions.ClubNotFoundException;
import com.RenToU.rentserver.exceptions.DuplicateMemberException;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.exceptions.ProductNotFoundException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
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
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;

    private final ProductRepository productRepository;

    @Override
    public List<Club> findClubs() {
        return clubRepository.findAll();
    }

    @Override
    @Transactional
    public ClubDTO createClub(Long memberId, String clubName, String clubIntro, String thumbnailPath, List<String> clubHashtags ) {
        if (clubRepository.findByName(clubName).orElse(null) != null) {
            //TODO DuplicateClubException으로 교체?
            throw new DuplicateMemberException("이미 존재하는 모임 이름입니다.");
        }
        //회원 조회
        Member member = findMember(memberId);
        //클럽 생성
        Club club = Club.createClub(clubName, clubIntro, thumbnailPath, member, clubHashtags);
        clubRepository.save(club);
        return ClubDTO.from(club);
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

    /**
     *Notification
     */
    @Override
    @Transactional
    public NotificationDTO createNotification(Long clubId, Long writerId, NotificationDTO notificationDTO) {
        Club club = findClub(clubId);
        Member writer = findMember(writerId);
        club.findClubMemberByMember(writer).validateAdmin();
        Notification notification = mapper.map(notificationDTO,Notification.class);
        club.addNotification(notification);
        clubRepository.save(club);
        return NotificationDTO.from(notification);
    }
    /**
     * product
     */
    @Override
    @Transactional
    public void registerProduct(Long clubId, ProductDTO productDTO, Long memberId){
        Club club = findClub(clubId);
        Member requester = findMember(memberId);
        club.findClubMemberByMember(requester).validateAdmin();
        Product product = mapper.map(productDTO,Product.class);
        club.addProduct(product);
        clubRepository.save(club);
    }
    @Override
    @Transactional
    public void registerItem(Long productId, Long memberId){
        Product product = findProduct(productId);
        Member requester = findMember(memberId);
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

    private Member findMember(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }
    private Product findProduct(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
    private Club findClub(Long id){
        return clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException(id));
    }

}
