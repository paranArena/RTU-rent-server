package com.RenToU.rentserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import static javax.persistence.FetchType.LAZY;

@Getter @Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;
    @OneToOne(mappedBy = "location", fetch = LAZY)
    private Product product;
    private Double x;
    private Double y;
    public Location(Double x, Double Y){
        this.x = x;
        this.y = y;
    }
}
