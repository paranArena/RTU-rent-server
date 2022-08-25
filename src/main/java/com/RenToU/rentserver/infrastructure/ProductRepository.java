package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
