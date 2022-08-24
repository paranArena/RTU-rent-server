package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Direction;
import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalHistory;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.exceptions.NotInRangeException;
import com.RenToU.rentserver.exceptions.ProductNotFoundException;
import com.RenToU.rentserver.exceptions.RentalNotFoundException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.ItemRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
import com.RenToU.rentserver.infrastructure.RentalHistoryRepository;
import com.RenToU.rentserver.infrastructure.RentalRepository;
import com.RenToU.rentserver.util.GeometryUtil;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.Query;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RentalService {
    private final EntityManager em;
    private final Mapper mapper;
    private final RentalRepository rentalRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final RentalHistoryRepository rentalHistoryRepository;
    @Transactional
    public Rental requestRental(Long memberId, Long itemId) {
        Item item = findItem(itemId);
        Member member = findMember(memberId);
        item.validateRentable();
        Rental rental = Rental.createRental(item,member);
        rentalRepository.save(rental);
        return rental;
    }
    @Transactional
    public Rental applyRental(Long memberId, Long itemId, Location location) {
        Item item = findItem(itemId);
        Member member = findMember(memberId);
        Rental rental = findRentalByItem(item);
        rental.validateWaiting();
        rental.validateMember(member);
        isInTheRange(location,item.getProduct());
        rental.startRental();
        rentalRepository.save(rental);
        return rental;
    }
    @Transactional
    public void returnRental(Long memberId, Long itemId, Location location){
        Item item = findItem(itemId);
        Member member = findMember(memberId);
        Rental rental = findRentalByItem(item);
        rental.validateRent();
        rental.validateMember(member);
        isInTheRange(location,item.getProduct());
        rental.finishRental();
        RentalHistory rentalHistory = RentalHistory.RentalToHistory(rental);
        rentalHistoryRepository.save(rentalHistory);
        rentalRepository.deleteById(rental.getId());
    }



    private Member findMember(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }
    private Item findItem(Long id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
    private Rental findRentalByItem(Item item){
        return rentalRepository.findByItem(item)
                .orElseThrow(()-> new RentalNotFoundException());
    }

    private void isInTheRange(Location location, Product product){
        Double distance = GeometryUtil.distance(location.getLatitude(),location.getLongitude(),product.getLocation().getX(),product.getLocation().getY());
        if(distance > product.getPickupDistance()){
            throw new NotInRangeException(distance);
        }
    }
}
