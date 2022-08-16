package com.RenToU.rentserver.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {
    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;
}



