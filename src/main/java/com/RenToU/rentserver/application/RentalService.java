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
import java.util.Optional;
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
            throw new CustomException(RentalErrorCode.NOT_IN_SAME_CLUB); // 유저가 속해있는 클럽의 아이템이 아닙니다.
        }
        // TODO 못빌린다 -> 멤버가 클럽에 속해있냐(예외1), 아이템이 클럽에 속해있냐(예외2)
    }

    @Transactional(noRollbackFor = { CustomException.class })
    public void applyRental(Long memberId, Long itemId) {
        Member member = findMember(memberId);
        Item item = findItem(itemId);
        Rental rental = findRentalByItem(item);
        rental.validateWait();
        rental.validateMember(member);
        validateApplyTimeNotOver(rental);
        rental.startRental();
        rentalRepository.save(rental);
    }

    @Transactional
    public void returnRental(Long memberId, Long itemId) {
        Member member = findMember(memberId);
        Rental rental = findRentalByItem(findItem(itemId));
        rental.validateRent();
        rental.validateMember(member);
        rental.checkLate();
        rental.finishRental();
        RentalHistory rentalHistory = RentalHistory.RentalToHistory(rental);
        rentalHistoryRepository.save(rentalHistory);
        rentalRepository.deleteById(rental.getId());
    }

    @Transactional
    public void cancelRental(Long memberId, Long itemId) {
        Member member = findMember(memberId);
        Rental rental = findRentalByItem(findItem(itemId));
        rental.validateWait();
        rental.validateMember(member);
        rental.cancel();
        RentalHistory rentalHistory = RentalHistory.RentalToHistory(rental);
        rentalHistoryRepository.save(rentalHistory);
        rentalRepository.deleteById(rental.getId());
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
            throw new CustomException(RentalErrorCode.WAIT_TIME_EXPIRED);
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
            if (product.getItems() != null) {
                items.addAll(product.getItems());
            }
        });
        List<RentalHistory> histories = new ArrayList<>();
        items.stream().forEach(item -> {
            List<RentalHistory> searchHistory = findHistoriesByItem(item);
            if (searchHistory != null) {
                histories.addAll(searchHistory);
            }
        });
        return histories;
    }

    @Transactional(noRollbackFor = { CustomException.class })
    public void justRental(Long adminId, Long clubId, Long itemId, String studentName, String studentId) {
        Member admin = findMember(adminId);
        Club club = findClub(clubId);
        Member member = findOrCreateTempMember(studentName, studentId, club);
        Item item = findItem(itemId);
        club.findClubMemberByMember(admin).validateAdmin();
        item.validateRentable();
        Rental rental = Rental.createRental(item, member);
        rental.startRental();
        rentalRepository.save(rental);
    }

    private Member findOrCreateTempMember(String studentName, String studentId, Club club) {
        Optional<Member> member = memberRepository.findOneWithAuthoritiesByStudentId(studentId);
        if (member.isPresent()) {
            return member.get();
        } else {
            Member tmpMember = Member.createTempMember(studentName, studentId, club);

            ClubMember clubMember = ClubMember.createClubMember(club, tmpMember, ClubRole.USER);
            memberRepository.save(tmpMember);
            return tmpMember;
        }

    }

    @Transactional
    public void returnRentalAdmin(Long adminId, Long clubId, Long itemId, Long memberId) {
        Member admin = findMember(adminId);
        Member member = findMember(memberId);
        Rental rental = findRentalByItem(findItem(itemId));
        Club club = findClub(clubId);
        rental.validateRent();
        club.findClubMemberByMember(admin).validateAdmin();
        rental.validateMember(member);
        rental.checkLate();
        rental.finishRental();
        RentalHistory rentalHistory = RentalHistory.RentalToHistory(rental);
        rentalHistoryRepository.save(rentalHistory);
        rentalRepository.deleteById(rental.getId());
    }

    private List<RentalHistory> findHistoriesByItem(Item item) {
        return rentalHistoryRepository.findAllByItem(item);
    }

    @Transactional
    public void cancelRentalAdmin(Long adminId, Long clubId, Long itemId) {
        Member admin = findMember(adminId);
        Rental rental = findRentalByItem(findItem(itemId));
        Club club = findClub(clubId);
        club.findClubMemberByMember(admin).validateAdmin();
        rental.validateWait();
        rental.cancel();
        RentalHistory rentalHistory = RentalHistory.RentalToHistory(rental);
        rentalHistoryRepository.save(rentalHistory);
        rentalRepository.deleteById(rental.getId());
    }
}
