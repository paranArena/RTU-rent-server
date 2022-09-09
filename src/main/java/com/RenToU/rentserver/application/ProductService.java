package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.dto.service.AddItemServiceDto;
import com.RenToU.rentserver.dto.service.CreateProductServiceDto;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Item;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.RentalPolicy;
import com.RenToU.rentserver.dto.service.UpdateProductInfoServiceDto;
import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;
import com.RenToU.rentserver.exceptions.MemberErrorCode;
import com.RenToU.rentserver.infrastructure.ClubRepository;
import com.RenToU.rentserver.infrastructure.ItemRepository;
import com.RenToU.rentserver.infrastructure.MemberRepository;
import com.RenToU.rentserver.infrastructure.ProductRepository;
import com.RenToU.rentserver.infrastructure.RentalRepository;
import com.github.dozermapper.core.Mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {
    private final Mapper mapper;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final RentalRepository rentalRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Product registerProduct(CreateProductServiceDto dto) {
        Club club = findClub(dto.getClubId());
        Member requester = findMember(dto.getMemberId());
        club.findClubMemberByMember(requester).validateAdmin();
        Product product = mapper.map(dto, Product.class);
        product.initialSetting(club, dto.getRentalPolicies());
        productRepository.save(product);
        clubRepository.save(club);
        return product;
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ClubErrorCode.PRODUCT_NOT_FOUND));
    }

    private Club findClub(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new CustomException(ClubErrorCode.CLUB_NOT_FOUND));
    }

    public List<Product> getProductsByClub(Long memberId, Long clubId) {
        Member member = findMember(memberId);
        Club club = findClub(clubId);
        club.findClubMemberByMember(member).validateUser();

        return club.getProducts();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ClubErrorCode.PRODUCT_NOT_FOUND));
    }

    public List<Product> getMyProducts(Long memberId) {
        Member member = findMember(memberId);
        List<Club> clubs = member.getClubListWithoutWait().stream().map(cm -> cm.getClub())
                .collect(Collectors.toList());
        List<Product> products = new ArrayList<>();
        clubs.stream().forEach(c -> products.addAll(c.getProducts()));
        return products;
    }

    @Transactional
    public Product updateProductInfo(Long productId, UpdateProductInfoServiceDto dto) {
        Club club = findClub(dto.getClubId());
        Member requester = findMember(dto.getMemberId());
        club.findClubMemberByMember(requester).validateAdmin();
        Product product = findProduct(productId);
        product.updateInfo(dto);
        productRepository.save(product);
        clubRepository.save(club);
        return product;
    }

    @Transactional
    public void deleteItemByNumbering(Long memberId, Long clubId, Long productId, int numbering) {
        Product product = findProduct(productId);
        if (clubId != product.getClub().getId())
            throw new CustomException(ClubErrorCode.PRODUCT_NOT_FOUND);
        Club club = findClub(clubId);// 영속성 컨텍스트 등록을 위해 repository로 검색
        Member requester = findMember(memberId);
        club.findClubMemberByMember(requester).validateAdmin();
        Item item = product.getItemByNumbering(numbering);
        if (item == null)
            throw new CustomException(ClubErrorCode.ITEM_NOT_FOUND);
        Rental rental = item.getRental();
        if (rental != null) {
            rental.deleteRental();
            rentalRepository.deleteById(rental.getId());
        }
        item.deleteProduct();
        itemRepository.deleteById(item.getId());
    }

    @Transactional
    public void addItem(Long memberId, Long clubId, Long productId, AddItemServiceDto dto) {
        Product product = findProduct(productId);
        if (clubId != product.getClub().getId())
            throw new CustomException(ClubErrorCode.PRODUCT_NOT_FOUND);
        Club club = findClub(clubId);// 영속성 컨텍스트 등록을 위해 repository로 검색
        Member requester = findMember(memberId);
        club.findClubMemberByMember(requester).validateAdmin();
        Item item = Item.createItem(product, dto.getRentalPolicy(), dto.getNumbering());
        itemRepository.save(item);
    }

    @Transactional
    public void deleteProduct(Long memberId, Long clubId, Long productId) {
        Product product = findProduct(productId);
        if (clubId != product.getClub().getId())
            throw new CustomException(ClubErrorCode.PRODUCT_NOT_FOUND);
        Club club = findClub(clubId);// 영속성 컨텍스트 등록을 위해 repository로 검색
        Member requester = findMember(memberId);
        club.findClubMemberByMember(requester).validateAdmin();
        product.deleteClub();
        productRepository.deleteById(productId);
    }

    private Item findItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new CustomException(ClubErrorCode.ITEM_NOT_FOUND));
    }
}
