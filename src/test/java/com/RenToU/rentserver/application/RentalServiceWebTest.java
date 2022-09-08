package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.domain.RentalHistory;
import com.RenToU.rentserver.domain.RentalPolicy;
import com.RenToU.rentserver.domain.RentalStatus;
import com.RenToU.rentserver.exceptions.CannotRentException;
import com.RenToU.rentserver.exceptions.CustomException;
import com.RenToU.rentserver.exceptions.MemberErrorCode;
import com.RenToU.rentserver.exceptions.RentalErrorCode;
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
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("rentalService 통합 테스트")
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
    @Autowired
    private ClubRepository clubRepository;
    private Long memberId;
    private Long itemId;
    private Long anotherMemberId;

    private Long rentalId;

    @BeforeEach
    void setUp() {
        service = new RentalService(rentalRepository, memberRepository, itemRepository,
                rentalHistoryRepository, clubRepository);
    }

    @Nested
    @DisplayName("requestRentalt메소드는")
    class Describe_requestRental {
        @Nested
        @DisplayName("member와 item이 같은 그룹내에 있지 않을 때")
        class not_in_same_club {
            @BeforeEach
            void setup() {
                memberId = 3L;
                itemId = 1L;
            }

            @Test
            @DisplayName(" CannotRent Exception을 던진다.")
            void it_throws_CannotRentException() {
                // NOTE: 이렇게 예외 테스트하면 될 것 같습니다!
                // 근데 지금은 MemberErrorCode.MEMBER_NOT_FOUND 가 뜨네요.
                assertThatThrownBy(() -> service.requestRental(memberId, itemId))
                        .isInstanceOf(CustomException.class)
                        .hasFieldOrPropertyWithValue("errorCode", RentalErrorCode.NOT_CLUB_MEMBER);
            }
        }

        @Nested
        @DisplayName("item이 렌탈할 수 있는 상태일 때")
        class can_rental {
            @BeforeEach
            void setup() {
                memberId = 5L;
                itemId = 1L;
            }

            @Test
            @DisplayName("새로운 렌탈을 생성한 후 리턴한다.")
            void it_returns_new_rental() {
                Member member = memberRepository.findById(memberId).get();
                Item item = itemRepository.findById(itemId).get();
                Rental rental = service.requestRental(memberId, itemId);
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
            void setup() {
                memberId = 5L;
                anotherMemberId = 4L;
                itemId = 1L;
            }

            @Test
            @DisplayName(" CannotRent Exception을 던진다.")
            void it_throws_CannotRentException() {
                Rental rental = service.requestRental(memberId, itemId);
                assertThatThrownBy(() -> service.requestRental(anotherMemberId, itemId))
                        .isInstanceOf(CannotRentException.class);
            }
        }
    }

    @Nested
    @DisplayName("applyRentalt메소드는")
    class Describe_applyRental {
        @Nested
        @DisplayName("wait상태인 rental이 주어졌을 때")
        class data_given {
            @BeforeEach
            void setup() {
                memberId = 4L;
                itemId = 1L;
                rentalId = service.requestRental(memberId, itemId).getId();
            }

            @Test
            @DisplayName("rentalStatus를 CANCEL로 바꾼 뒤 렌탈을 삭제하고 rentalHistory를 리턴한다.")
            void it_changes_rentalStatus_To_Cancel_and_delete_rental() {
                RentalHistory rentalHistory = service.cancelRental(memberId, rentalId);
                assertThat(rentalHistory.getItem().getId()).isEqualTo(itemId);
                assertThat(rentalHistory.getRentalStatus()).isEqualTo(RentalStatus.CANCEL);
                assertThat(rentalHistory.getMember().getId()).isEqualTo(memberId);
                assertThat(rentalHistory.getItem().getRental()).isNull();
            }
        }

        @Nested
        @DisplayName("렌탈 대기 시간이 끝나고 요청되었을 때")
        class if_WaitTime_is_over {
            @BeforeEach
            void setup() {
                memberId = 4L;
                itemId = 1L;
                Rental rental = service.requestRental(memberId, itemId);
                rental.setRentDateBeforeTenM();
                rentalRepository.save(rental);
                rentalId = rental.getId();
            }

            @Test
            @DisplayName("CannotRentException을 던진다.")
            void it_throws_cannotRentException() {
                assertThatThrownBy(() -> service.applyRental(memberId, rentalId))
                        .isInstanceOf(CannotRentException.class);
                assertThatThrownBy(() -> rentalRepository.findById(rentalId).get())
                        .isInstanceOf(NoSuchElementException.class);
            }
        }
    }

    @Nested
    @DisplayName("cancelRental메소드는")
    class Describe_cancelRental {
        @Nested
        @DisplayName("wait상태인 rental이 주어졌을 때")
        class data_given {
            @BeforeEach
            void setup() {
                memberId = 4L;
                itemId = 1L;
                rentalId = service.requestRental(memberId, itemId).getId();
            }

            @Test
            @DisplayName("rentalStatus를 CANCEL로 바꾼 뒤 렌탈을 삭제하고 rentalHistory를 리턴한다.")
            void it_changes_rentalStatus_To_Cancel_and_delete_rental() {
                RentalHistory rentalHistory = service.cancelRental(memberId, rentalId);
                assertThat(rentalHistory.getItem().getId()).isEqualTo(itemId);
                assertThat(rentalHistory.getRentalStatus()).isEqualTo(RentalStatus.CANCEL);
                assertThat(rentalHistory.getMember().getId()).isEqualTo(memberId);
                assertThat(rentalHistory.getItem().getRental()).isNull();
                assertThatThrownBy(() -> rentalRepository.findById(rentalId).get())
                        .isInstanceOf(NoSuchElementException.class);
            }
        }
    }

    @Nested
    @DisplayName("returnRental메소드는")
    class Describe_returnRental {
        @Nested
        @DisplayName("RENT상태인 rental이 주어지고, 제한 시간 안에 요청했을 때")
        class data_given_in_time {
            @BeforeEach
            void setup() {
                memberId = 4L;
                itemId = 1L;
                rentalId = service.requestRental(memberId, itemId).getId();
                service.applyRental(memberId, rentalId);
            }

            @Test
            @DisplayName("rentalStatus를 DONE으로 바꾼 뒤 렌탈을 삭제하고 rentalHistory를 리턴한다.")
            void it_changes_rentalStatus_To_Cancel_and_delete_rental() {
                RentalHistory rentalHistory = service.returnRental(memberId, rentalId);
                assertThat(rentalHistory.getItem().getId()).isEqualTo(itemId);
                assertThat(rentalHistory.getRentalStatus()).isEqualTo(RentalStatus.DONE);
                assertThat(rentalHistory.getMember().getId()).isEqualTo(memberId);
                assertThat(rentalHistory.getItem().getRental()).isNull();
                assertThatThrownBy(() -> rentalRepository.findById(rentalId).get())
                        .isInstanceOf(NoSuchElementException.class);
            }
        }
    }

    @Nested
    @DisplayName("RENT상태인 rental이 주어지고, 제한 시간 안에 요청했을 때")
    class data_given_in_time {
        @BeforeEach
        void setup() {
            memberId = 4L;
            itemId = 1L;
            rentalId = service.requestRental(memberId, itemId).getId();
            service.applyRental(memberId, rentalId);
        }

        @Test
        @DisplayName("rentalStatus를 DONE으로 바꾼 뒤 렌탈을 삭제하고 rentalHistory를 리턴한다.")
        void it_changes_rentalStatus_To_Cancel_and_delete_rental() {
            RentalHistory rentalHistory = service.returnRental(memberId, rentalId);
            assertThat(rentalHistory.getItem().getId()).isEqualTo(itemId);
            assertThat(rentalHistory.getRentalStatus()).isEqualTo(RentalStatus.DONE);
            assertThat(rentalHistory.getMember().getId()).isEqualTo(memberId);
            assertThat(rentalHistory.getItem().getRental()).isNull();
            assertThatThrownBy(() -> rentalRepository.findById(rentalId).get())
                    .isInstanceOf(NoSuchElementException.class);
        }
    }
}
