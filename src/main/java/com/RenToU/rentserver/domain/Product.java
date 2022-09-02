package com.RenToU.rentserver.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

//물품 ex) 책, 우산
@Entity
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Product extends BaseTimeEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id
    @Column(name = "product_id")
    private Long id;

    private String name;

    private String category;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

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
    }
    //클럽과의 관계 생성, item 생성
    public void initialSetting(Club club, List<RentalPolicy> policies) {
        this.setClub(club);
        this.setLocation(location);
        for(int i = 1; i <= policies.size(); i++){
            Item item = Item.createItem(this,policies.get(i-1),i);
            this.addItem(item);
        }
    }

    private void setLocation(Location location) {
        location.setProduct(this);
    }

    public static Product createProduct(String name, String category,Location location,int fifoRentalPeriod, int reserveRentalPeriod,int price, String caution,String imagePath){
        Product product = Product.builder()
                .name(name)
                .category(category)
                .location(location)
                .fifoRentalPeriod(fifoRentalPeriod)
                .reserveRentalPeriod(reserveRentalPeriod)
                .price(price)
                .caution(caution)
                .imagePath(imagePath)
                .build();
        product.items = new ArrayList<>();
        return product;
    }



    /**
     * 비즈니스 로직
     */

}
