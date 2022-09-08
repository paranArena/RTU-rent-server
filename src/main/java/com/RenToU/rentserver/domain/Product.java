package com.RenToU.rentserver.domain;

import com.RenToU.rentserver.dto.service.UpdateProductInfoServiceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
import java.util.stream.Collectors;

//물품 ex) 책, 우산
@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Product extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_id")
    private Long id;

    private String name;

    private String category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    private int fifoRentalPeriod;

    private int reserveRentalPeriod;

    private int price;

    private String caution;

    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    /**
     * 연관관계 메소드
     */
    public void addItem(Item item) {
        items.add(item);
        item.setProduct(this);
    }

    public void setClub(Club club) {
        this.club = club;
    }

    // 클럽과의 관계 생성, item 생성
    public void initialSetting(Club club, List<RentalPolicy> policies) {
        this.setClub(club);
        this.setLocationJpa(location);
        for (int i = 1; i <= policies.size(); i++) {
            Item item = Item.createItem(this, policies.get(i - 1), i);
            this.addItem(item);
        }
    }

    private void setLocationJpa(Location location) {
        location.setProduct(this);
    }

    public static Product createProduct(String name, String category, Location location, int fifoRentalPeriod,
            int reserveRentalPeriod, int price, String caution, String imagePath) {
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

    public void updateInfo(UpdateProductInfoServiceDto dto) {
        this.name = dto.getName();
        this.category = dto.getCategory();
        this.fifoRentalPeriod = dto.getFifoRentalPeriod();
        this.reserveRentalPeriod = dto.getReserveRentalPeriod();
        this.price = dto.getPrice();
        this.caution = dto.getCaution();
        this.category = dto.getCategory();
        this.location = dto.getLocation();
        if (imagePath != null) {
            this.imagePath = dto.getImagePath();
        }
    }

    public Item getItemByNumbering(int numbering) {
        List<Item> items = this.getItems().stream().filter(i -> i.getNumbering() == numbering)
                .collect(Collectors.toList());
        if (items.size() == 0) {
            return null;
        } else {
            return items.get(0);
        }
    }

    public void deleteItem(Item item) {
        this.items.remove(item);
    }

    public void deleteClub() {
        this.club.deleteProduct(this);
        this.club = null;
    }

    /**
     * 비즈니스 로직
     */

}
