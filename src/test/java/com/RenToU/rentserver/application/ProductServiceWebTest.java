package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.RentalPolicy;
import com.RenToU.rentserver.dto.service.ProductServiceDto;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.HashtagRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
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
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@DisplayName("productService 통합 테스트")
class ProductServiceWebTest {
    private ProductService service;
    @Autowired
    private ClubService clubService;
    @Autowired
    private Mapper mapper;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ClubRepository clubRepository;
    @Autowired
    ProductRepository productRepository;
    private Member owner;
    private Club club;



    @BeforeEach
    void setUp() {
        service = new ProductService(mapper, clubRepository, memberRepository, productRepository);
        owner = memberRepository.findById(1L).get();
        club = clubService.createClub(owner.getId(),"club","intro","www.com",new ArrayList<>());


    }

    @Nested
    @DisplayName("registerProduct메소드는")
    class Describe_registerProduct {
        @Nested
        @DisplayName("dto가 주어지면")
        class data_given {
            @Test
            @DisplayName("새 product를 생성하고 리턴한다.")
            void it_return_new_club() {
                Location location = new Location(1.0,1.0);
                ProductServiceDto dto = new ProductServiceDto(club.getId(),owner.getId(),"카메라","전자기기",location,3,3,100000,"caution","www.com", List.of(RentalPolicy.FIFO,RentalPolicy.FIFO,RentalPolicy.RESERVE));
                Product product = service.registerProduct(dto);
                assertThat(product.getName()).isEqualTo("카메라");
                assertThat(product.getLocation().getX()).isEqualTo(location.getX());
                assertThat(product.getLocation().getY()).isEqualTo(location.getY());
            }
        }
    }
}