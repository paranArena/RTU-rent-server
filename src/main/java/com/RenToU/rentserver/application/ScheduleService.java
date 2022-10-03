package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalHistory;
import com.RenToU.rentserver.domain.RentalStatus;
import com.RenToU.rentserver.infrastructure.ClubMemberRepository;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.RentalHistoryRepository;
import com.RenToU.rentserver.infrastructure.RentalRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void checkExpiredRentalWait() {
        List<Rental> rentals = rentalRepository.findAllByRentalStatus(RentalStatus.WAIT);
        if (rentals != null) {
            rentals.stream().forEach(rental -> {
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
                .stream().map(cm->cm.getMember().getId()).collect(Collectors.toList());
        for(Long i = 1L; i <= 200L; i++){
            if(cmId.contains(i)){
                continue;
            }
            Optional<Member> member = memberRepository.findById(i);
            if(member.isPresent()){
                clubMemberRepository.save(ClubMember.createClubMember(club,member.get(), ClubRole.USER));
            }
        }
    }
}
