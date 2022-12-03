package com.RenToU.rentserver.event;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.Rental;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RentalExpirationRemindEvent {
    private final Long day;
    private final Club club;
    private final Product product;
    private final Rental rental;
    private final Member member;
}
