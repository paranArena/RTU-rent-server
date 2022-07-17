package com.RenToU.rentserver.infrastructure;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Product;
import com.RenToU.rentserver.exceptions.ClubNotFoundException;
import com.RenToU.rentserver.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JPAProductRepository implements ProductRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Product> findAll() {
        return em.createQuery("select p from Product p", Product.class)
                .getResultList();
    }

    @Override
    public Product findById(Long id) {
        return Optional.of(em.find(Product.class,id))
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product save(Product product) {
        em.persist(product);
        return product;
    }

    @Override
    public void delete(Product product) {
        em.remove(product);
    }
}
