package com.RenToU.rentserver.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public enum ClubRole {
    OWNER,ADMIN,USER,WAIT
}
