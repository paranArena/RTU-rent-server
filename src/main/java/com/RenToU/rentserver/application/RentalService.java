package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalHistory;
import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;
import com.RenToU.rentserver.exceptions.MemberErrorCode;
import com.RenToU.rentserver.exceptions.RentalErrorCode;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.ItemRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.RentalHistoryRepository;
import com.RenToU.rentserver.infrastructure.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RentalService {
    private final RentalRepository rentalRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final RentalHistoryRepository rentalHistoryRepository;
    private final ClubRepository clubRepository;

    @Transactional
    public Rental requestRental(Long memberId, Long itemId) {
        Item item = findItem(itemId);
        Member member = findMember(memberId);
        checkIsInSameClub(member, item);
        item.validateRentable();
        Rental rental = Rental.createRental(item, member);
        rentalRepository.save(rental);
        return rental;
    }

    private void checkIsInSameClub(Member member, Item item) {
        List<ClubMember> userClubMembers = member.getClubList().stream().filter(cm -> cm.getRole() != ClubRole.WAIT)
                .collect(Collectors.toList());
        List<Club> clubs = userClubMembers.stream().map(cm -> cm.getClub()).collect(Collectors.toList());
        if (!clubs.contains(item.getProduct().getClub())) {
            throw new CustomException(RentalErrorCode.NOT_CLUB_MEMBER); // 유저가 속해있는 클럽의 아이템이 아닙니다.
        }
        // TODO 못빌린다 -> 멤버가 클럽에 속해있냐(예외1), 아이템이 클럽에 속해있냐(예외2)
    }

    @Transactional(noRollbackFor={CannotRentException.class})
    public void applyRental(Long memberId, Long rentalId) {
        Member member = findMember(memberId);
        Rental rental = findRental(rentalId);
        rental.validateWait();
        rental.validateMember(member);
        validateApplyTimeNotOver(rental);
        rental.startRental();
        rentalRepository.save(rental);
    }

    @Transactional
    // TODO void로 바꿔주실 수 있나요? 이 행위를 요청했을 때 resData는 null이 들어가도 될 것 같습니다.
    public RentalHistory returnRental(Long memberId, Long rentalId) {
        Member member = findMember(memberId);
        Rental rental = findRental(rentalId);
        rental.validateRent();
        rental.validateMember(member);
        rental.checkLate();
        rental.finishRental();
        RentalHistory rentalHistory = RentalHistory.RentalToHistory(rental);
        rentalHistoryRepository.save(rentalHistory);
        rentalRepository.deleteById(rental.getId());
        return rentalHistory;
    }

    @Transactional
    // TODO void로 바꿔주실 수 있나요? 이 행위를 요청했을 때 resData는 null이 들어가도 될 것 같습니다.
    public RentalHistory cancelRental(Long memberId, Long rentalId) {
        Member member = findMember(memberId);
        Rental rental = findRental(rentalId);
        rental.validateWait();
        rental.validateMember(member);
        rental.cancel();
        RentalHistory rentalHistory = RentalHistory.RentalToHistory(rental);
        rentalHistoryRepository.save(rentalHistory);
        rentalRepository.deleteById(rental.getId());
        return rentalHistory;
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Item findItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new CustomException(ClubErrorCode.ITEM_NOT_FOUND));
    }

    private Rental findRentalByItem(Item item) {
        return rentalRepository.findByItem(item)
                .orElseThrow(() -> new CustomException(RentalErrorCode.RENTAL_NOT_FOUND));
    }

    private Rental findRental(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new CustomException(RentalErrorCode.RENTAL_NOT_FOUND));
    }

    private Club findClub(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new CustomException(ClubErrorCode.CLUB_NOT_FOUND));
    }

    public void validateApplyTimeNotOver(Rental rental) {
        if (rental.getRentDate().plusMinutes(10).isBefore(LocalDateTime.now())) {
            cancelRental(rental.getMember().getId(), rental.getId());
            throw new CustomException(RentalErrorCode.WAIT_TIME_OVER);
        }
    }

    public List<Item> getRentalsByClub(Long clubId, Long memberId) {
        Club club = findClub(clubId);
        Member member = findMember(memberId);
        club.findClubMemberByMember(member).validateAdmin();
        List<Product> products = findClub(clubId).getProducts();
        List<Item> items = new ArrayList<>();
        products.stream().forEach(product -> {
            if (product.getItems() != null) {
                items.addAll(product.getItems());
            }
        });
        List<Item> rentalItems = items.stream()
                .filter(item -> item.getRental() != null)
                .collect(Collectors.toList());
        return rentalItems;
    }

    public List<RentalHistory> getRentalHistoryByClub(long clubId, Long memberId) {
        Club club = findClub(clubId);
        Member member = findMember(memberId);
        club.findClubMemberByMember(member).validateAdmin();
        List<Product> products = findClub(clubId).getProducts();
        List<Item> items = new ArrayList<>();
        products.stream().forEach(product -> {
            if(product.getItems()!= null) {
                items.addAll(product.getItems());
            }
        });
        List<RentalHistory> histories = new ArrayList<>();
        items.stream().forEach(item -> {
            List<RentalHistory> searchHistory = findHistoriesByItem(item);
            if(searchHistory != null) {
                histories.addAll(searchHistory);
            }
        });
        return histories;
    }

    private List<RentalHistory> findHistoriesByItem(Item item) {
        return rentalHistoryRepository.findAllByItem(item);
    }
}
