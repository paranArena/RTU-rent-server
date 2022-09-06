package com.RenToU.rentserver.domain;

import com.RenToU.rentserver.exceptions.CannotRentException;
import com.RenToU.rentserver.exceptions.ItemAlreadyExistException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.CascadeType;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
// 물건 ex:) 1번 우산, 2번 책
public class Item extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "item_id")
    private Long id;

    private int numbering;

    @Enumerated
    private RentalPolicy rentalPolicy;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private Product product;
    @OneToOne(mappedBy = "item", fetch = LAZY)
    private Rental rental;

    public static Item createItem(Product product, RentalPolicy rentalPolicy, int numbering) {
        if(product.getItemByNumbering(numbering) != null){
            throw new ItemAlreadyExistException();
        }
        Item item = Item.builder()
                .numbering(numbering)
                .rentalPolicy(rentalPolicy)
                .product(product)
                .build();
        return item;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * 비즈니스 로직
     */
    public void validateRentable() {
        if (this.rental != null) {
            throw new CannotRentException(this.id);
        }
    }

    public void finishRental() {
        this.rental = null;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
        rental.setItem(this);
    }


    public void deleteRental() {
        this.rental = null;

    }

    public void deleteProduct() {
        this.product.deleteItem(this);
        this.product = null;
    }

}
