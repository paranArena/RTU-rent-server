package com.RenToU.rentserver.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
//물품 ex) 책, 우산
@Entity
@Getter
@Setter
public class Product extends BaseTimeEntity{
    @GeneratedValue @Id
    @Column(name = "product_id")
    private Long id;

    private String name;

    private int sequence;

    private RentalPolicy rentalPolicy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    /**
     * 연관관계 메소드
     */
    public void addItem(Item item) {
        items.add(item);
        item.setProduct(this);
    }

    /**
     * 비즈니스 로직
     */
    public void addSeq() {
        this.sequence++;
    }


}
