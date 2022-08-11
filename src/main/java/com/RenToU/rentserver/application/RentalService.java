package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.exceptions.ProductNotFoundException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.ItemRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
import com.RenToU.rentserver.infrastructure.RentalRepository;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RentalService {
    private final Mapper mapper;
    private final RentalRepository rentalRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public Long requestRental(Long memberId, Long itemId) {
        Item item = findItem(itemId);
        Member member = findMember(memberId);
        item.validateRentable();
        Rental rental = Rental.createRental(item,member);

    }
    private Member findMember(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }
    private Item findItem(Long id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
