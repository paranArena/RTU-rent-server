package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.dto.service.ProductServiceDto;
import com.RenToU.rentserver.exceptions.ClubNotFoundException;
import com.RenToU.rentserver.exceptions.MemberNotFoundException;
import com.RenToU.rentserver.exceptions.ProductNotFoundException;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final Mapper mapper;

    @Transactional
    public void registerProduct(ProductServiceDto productServiceDto){
        Club club = findClub(productServiceDto.getClubId());
        Member requester = findMember(productServiceDto.getMemberId());
        club.findClubMemberByMember(requester).validateAdmin();
        Product product = mapper.map(productServiceDto, Product.class);
        club.addProduct(product);
        clubRepository.save(club);
    }
    @Transactional
    public void registerItem(Long productId, Long memberId){
        Product product = findProduct(productId);
        Member requester = findMember(memberId);
        Club club = product.getClub();
        club.findClubMemberByMember(requester).validateAdmin();
        product.addSeq();
        Item item = Item.createItem(product);
        product.addItem(item);
        productRepository.save(product);
    }
    private Member findMember(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }
    private Product findProduct(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
    private Club findClub(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException(id));
    }
}
