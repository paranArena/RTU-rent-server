package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalPolicy;
import com.RenToU.rentserver.domain.RentalStatus;
import com.RenToU.rentserver.dto.service.ProductServiceDto;
import com.RenToU.rentserver.exceptions.CannotRentException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.HashtagRepository;
import com.RenToU.rentserver.infrastructure.ItemRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
import com.RenToU.rentserver.infrastructure.RentalHistoryRepository;
import com.RenToU.rentserver.infrastructure.RentalRepository;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@DisplayName("productService 통합 테스트")
class RentalServiceWebTest {
    private RentalService service;
    @Autowired
    private Mapper mapper;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RentalHistoryRepository rentalHistoryRepository;
    private Long memberId;
    private Long itemId;
    private Long anotherMemberId;

    @BeforeEach
    void setUp() {
        service = new RentalService(mapper,rentalRepository,memberRepository,itemRepository,rentalHistoryRepository);
    }

    @Nested
    @DisplayName("requestRentalt메소드는")
    class Describe_registerProduct {
        @Nested
        @DisplayName("member와 item이 같은 그룹내에 있지 않을 때")
        class not_in_same_club {
            @BeforeEach
            void setup(){
                memberId = 3L;
                itemId = 1L;
            }
            @Test
            @DisplayName(" CannotRent Exception을 던진다.")
            void it_throws_CannotRentException() {
                assertThatThrownBy(()->service.requestRental(memberId,itemId))
                        .isInstanceOf(CannotRentException.class);
            }
        }
        @Nested
        @DisplayName("item이 렌탈할 수 있는 상태일 때")
        class can_rental {
            @BeforeEach
            void setup(){
                memberId = 5L;
                itemId = 1L;
            }
            @Test
            @DisplayName("새로운 렌탈을 생성한 후 리턴한다.")
            void it_returns_new_rental() {
                Member member = memberRepository.findById(memberId).get();
                Item item = itemRepository.findById(itemId).get();
                Rental rental = service.requestRental(memberId,itemId);
                assertThat(rental.getRentalStatus()).isEqualTo(RentalStatus.WAIT);
                assertThat(rental.getMember()).isEqualTo(member);
                assertThat(rental.getItem()).isEqualTo(item);
                assertThat(member.getRentals()).contains(rental);
                assertThat(item.getRental()).isEqualTo(rental);
            }
        }
        @Nested
        @DisplayName("item이 렌탈할 수 없는 상태일 때")
        class item_already_rented {
            @BeforeEach
            void setup(){
                memberId = 5L;
                anotherMemberId = 4L;
                itemId = 1L;
            }
            @Test
            @DisplayName(" CannotRent Exception을 던진다.")
            void it_throws_CannotRentException() {
                Rental rental = service.requestRental(memberId,itemId);
                assertThatThrownBy(()->service.requestRental(anotherMemberId,itemId))
                        .isInstanceOf(CannotRentException.class);
            }
        }
    }
}