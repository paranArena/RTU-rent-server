package com.RenToU.rentserver.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
public enum Authority {
    ADMIN, USER
}


