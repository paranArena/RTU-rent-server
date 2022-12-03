package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.*;
import com.RenToU.rentserver.event.RentalExpirationRemindEvent;
import com.RenToU.rentserver.infrastructure.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final RentalRepository rentalRepository;
    private final RentalHistoryRepository rentalHistoryRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void checkExpiredRentalWait() {
        List<Rental> rentals = rentalRepository.findAllByRentalStatus(RentalStatus.WAIT);
        if (rentals != null) {
            rentals.forEach(rental -> {
                if (rental.getRentDate().plusMinutes(10).isBefore(LocalDateTime.now())) {
                    rental.validateWait();
                    rental.cancel();
                    RentalHistory rentalHistory = RentalHistory.RentalToHistory(rental);
                    rentalHistoryRepository.save(rentalHistory);
                    rentalRepository.deleteById(rental.getId());
                }
            });
        }
    }

    @Transactional
    public void joinRen2UAll() {
        Club club = clubRepository.findById(19L).get();
        List<Long> cmId = clubMemberRepository.findAllByClubId(19L)
                .stream().map(cm -> cm.getMember().getId()).collect(Collectors.toList());
        for (Long i = 1L; i <= 200L; i++) {
            if (cmId.contains(i)) {
                continue;
            }
            Optional<Member> member = memberRepository.findById(i);
            member.ifPresent(value -> clubMemberRepository.save(ClubMember.createClubMember(club, value, ClubRole.USER)));
        }
    }

    public void checkExpiredRental(long day) {
        List<Rental> rentals = rentalRepository.findAllByRentalStatus(RentalStatus.RENT);
        if (rentals != null) {
            List<Rental> expiredSoon = rentals.stream().filter(
                    rental -> LocalDateTime.now().plusDays(day + 1).isAfter(rental.getExpDate())
            ).collect(Collectors.toList());
            expiredSoon.forEach(rental -> {
                Member member = rental.getMember();
                Product product = rental.getItem().getProduct();
                Club club = product.getClub();
                member.getFcmToken(); // init proxy
                club.getName(); // init proxy
                product.getName(); // init proxy


                eventPublisher.publishEvent(new RentalExpirationRemindEvent(day, club, product, rental, member));
            });
        }
    }
}
