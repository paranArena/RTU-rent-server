package com.RenToU.rentserver.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Club {
    @Id
    @GeneratedValue
    @Column(name = "club_id")
    private Long id;

    private String name;

    private String thumbnailPath;

    private String introduction;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "club")
    private List<ClubMember> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "club")
    private List<Notification> notifications = new ArrayList<>();

}
