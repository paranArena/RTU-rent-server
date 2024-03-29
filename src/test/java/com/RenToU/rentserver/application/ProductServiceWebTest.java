package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.RentalPolicy;
import com.RenToU.rentserver.dto.service.CreateProductServiceDto;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.ItemRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
import com.RenToU.rentserver.infrastructure.RentalRepository;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("productService 통합 테스트")
class ProductServiceWebTest {
    private ProductService service;
    @Autowired
    private Mapper mapper;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ClubRepository clubRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    ItemRepository itemRepository;
    private Member owner;
    private Club club;

    @BeforeEach
    void setUp() {
        service = new ProductService(mapper, clubRepository, memberRepository, productRepository, rentalRepository,
                itemRepository);
        owner = memberRepository.findById(2L).get();
        club = clubRepository.findById(1L).get();

    }

    @Nested
    @DisplayName("registerProduct메소드는")
    class Describe_registerProduct {
        @Nested
        @DisplayName("dto가 주어지면")
        class data_given {
            @Test
            @DisplayName("새 product를 생성하고 리턴한다.")
            void it_return_new_product() {
                Location location = new Location("Test Location", 1.0, 1.0);
                CreateProductServiceDto dto = new CreateProductServiceDto(club.getId(), owner.getId(), null, "카메라",
                        "전자기기", 100000, List.of(RentalPolicy.FIFO, RentalPolicy.FIFO, RentalPolicy.RESERVE), 3, 3,
                        location, "caution");
                Product product = service.registerProduct(dto);
                assertThat(product.getName()).isEqualTo("카메라");
                assertThat(product.getLocation().getX()).isEqualTo(location.getX());
                assertThat(product.getLocation().getY()).isEqualTo(location.getY());
            }

            @Test
            @DisplayName("item을 rentalPreiod 개수만큼 생성한다.")
            void it_create_item_based_on_rentalPeriod_size() {
                Location location = new Location("Test Location", 1.0, 1.0);
                CreateProductServiceDto dto = new CreateProductServiceDto(club.getId(), owner.getId(), null, "카메라",
                        "전자기기", 100000, List.of(RentalPolicy.FIFO, RentalPolicy.FIFO, RentalPolicy.RESERVE), 3, 3,
                        location, "caution");
                Product product = service.registerProduct(dto);
                assertThat(product.getItems().size()).isEqualTo(3);
                assertThat(product.getItems().get(0).getRentalPolicy()).isEqualTo(RentalPolicy.FIFO);
                assertThat(product.getItems().get(0).getNumbering()).isEqualTo(1);
            }
        }
    }
}