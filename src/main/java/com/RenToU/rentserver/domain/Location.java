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
    public Location(Double x, Double y, String name){
        this.x = x;
        this.y = y;
        this.name = name;
    }
    public Location(Double x, Double y){
        this.x = x;
        this.y = y;
    }
    @Id
    @GeneratedValue
    @Column(name = "location_id")
    private Long id;
    private String name;
    private Double x;
    private Double y;
    @OneToOne(mappedBy = "location", fetch = LAZY)
    private Product product;
}
