package com.RenToU.rentserver.infrastructure.jpa;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph("product.ItemRental")
    List<Product> findAllByClubId(Long clubId);
}
