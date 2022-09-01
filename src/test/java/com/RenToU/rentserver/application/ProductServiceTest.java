package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.RentalPolicy;
import com.RenToU.rentserver.dto.service.CreateProductServiceDto;
import com.RenToU.rentserver.infrastructure.ClubHashtagRepository;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.HashtagRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService service;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private Mapper mapper;
    @Mock
    private ClubRepository clubRepository;
    Location location = new Location(1.0,1.0);
    @Test
    @DisplayName("product 등록 테스트")
    public void registerProductTest() throws Exception {
        //given
        Member member = Member.createMember("test","email@email.com");
        Club club = Club.createClub("club","intro","www.com",member,new ArrayList<>());
        //when
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        CreateProductServiceDto productServiceDto = new CreateProductServiceDto(1L,1L,"카메라","전자기기",3,location,3,3,100000,"caution","www.com", List.of(RentalPolicy.FIFO,RentalPolicy.FIFO,RentalPolicy.RESERVE));
        Product mappedProduct = Product.createProduct("카메라","전자기기",3,location,3,3,100000,"caution","www.com");
        when(mapper.map(productServiceDto,Product.class)).thenReturn(mappedProduct);
        Product product = service.registerProduct(productServiceDto);
        //then
        assertThat(product.getName()).isEqualTo("카메라");
        assertThat(product.getItems().size()).isEqualTo(3);
        assertThat(product.getItems().get(0).getNumbering()).isEqualTo(1);
        assertThat(product.getItems().get(0).getRentalPolicy()).isEqualTo(RentalPolicy.FIFO);
    }

}