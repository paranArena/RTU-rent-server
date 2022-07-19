package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Member;
import com.RenToU.rentserver.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
