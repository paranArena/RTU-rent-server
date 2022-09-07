package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalHistory;
import com.RenToU.rentserver.exceptions.CannotRentException;
import com.RenToU.rentserver.exceptions.ItemNotFoundException;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.exceptions.ProductNotFoundException;
import com.RenToU.rentserver.exceptions.RentalNotFoundException;
import com.RenToU.rentserver.exceptions.club.ClubNotFoundException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.ItemRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.RentalHistoryRepository;
import com.RenToU.rentserver.infrastructure.RentalRepository;
import com.github.dozermapper.core.Mapper;
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
    private final Mapper mapper;
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
            throw new CannotRentException(item.getId());
        }
    }

    @Transactional
    public Rental applyRental(Long memberId, Long rentalId) {
        Member member = findMember(memberId);
        Rental rental = findRental(rentalId);
        rental.validateWait();
        rental.validateMember(member);
        validateApplyTimeNotOver(rental);
        rental.startRental();
        rentalRepository.save(rental);
        return rental;
    }

    @Transactional
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
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    private Item findItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    private Rental findRentalByItem(Item item) {
        return rentalRepository.findByItem(item)
                .orElseThrow(() -> new RentalNotFoundException());
    }

    private Rental findRental(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }
    private Club findClub(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException(id));
    }

    public void validateApplyTimeNotOver(Rental rental) {
        if (rental.getRentDate().plusMinutes(10).isBefore(LocalDateTime.now())) {
            cancelRental(rental.getMember().getId(), rental.getId());
            throw new CannotRentException(rental.getId());
        }
    }

    public List<Rental> getRentalsByClub(Long clubId, Long memberId) {
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
        List<Rental> rentals = items.stream()
                .filter(item-> item.getRental() != null)
                .map(item->item.getRental())
                .collect(Collectors.toList());
        return rentals;
    }

}
