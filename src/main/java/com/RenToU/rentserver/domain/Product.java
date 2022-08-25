package com.RenToU.rentserver.domain;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;

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
public class Product extends BaseTimeEntity{
    @GeneratedValue @Id
    @Column(name = "product_id")
    private Long id;

    private String name;

    private String category;

    private int quantity;

    private Point location;

    private int fifoRentalPeriod;

    private int reserveRentalPeriod;

    private int price;

    private String caution;

    private String imagePath;

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
    public void setClub(Club club){
        this.club = club;
        club.addProduct(this);

    }

    /**
     * 비즈니스 로직
     */

}
