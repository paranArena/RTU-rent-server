package com.RenToU.rentserver.infrastructure.club;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.domain.Rental;
import com.RenToU.rentserver.dto.response.preview.ProductPreviewDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.RenToU.rentserver.domain.QClub.club;
import static com.RenToU.rentserver.domain.QClubMember.clubMember;
import static com.RenToU.rentserver.domain.QItem.item;
import static com.RenToU.rentserver.domain.QProduct.product;
import static com.RenToU.rentserver.domain.QRental.rental;

@RequiredArgsConstructor
@Repository
public class ClubQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Product> getMyProducts(Long memberId) {
        return queryFactory.select(product)
                .from(clubMember)
                .where(clubMember.member.id.eq(memberId))
                .where(clubMember.role.ne(ClubRole.WAIT))
                .leftJoin(clubMember.club, club)
                .distinct()
                .fetchJoin()
                .fetch();
    }
    public List<Product> searchRentals(Product p) {
        return queryFactory.select(product)
                .from(product)
                .leftJoin(product.items,item)
                .fetchJoin()
                .leftJoin(item.rental, rental)
                .fetchJoin()
                .fetch();
    }
}