package com.RenToU.rentserver.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL)
    private List<MemberAuthority> memberAuths = new ArrayList<>();


    public static Authority createAuth(String role) {
        Authority auth = Authority.builder()
                .authorityName(role)
                .build();
        auth.memberAuths = new ArrayList<>();
        return auth;
    }
    public void addMemberAuth(MemberAuthority memberAuthority) {
        this.memberAuths.add(memberAuthority);
        memberAuthority.setAuthority(this);
    }
}



