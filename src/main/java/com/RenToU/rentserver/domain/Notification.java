package com.RenToU.rentserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String title;

    private String content;

    private String imagePath;

    private Boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "club_id")
    private Club club;

    public static Notification createNotification(String title, String content, String imagePath,
            Club club) {
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .imagePath(imagePath)
                .isPublic(true)
                .build();
        club.addNotification(notification);
        notification.setClub(club);
        return notification;
    }

    public void changeIsPublic() {
        this.isPublic = !isPublic;
    }

    public void update(String title, String content, List<String> imagePaths, Boolean isPublic) {
        this.title = title;
        this.content = content;
        if (imagePaths != null && !imagePaths.isEmpty()) {
            this.imagePath = imagePaths.get(0);
        } else {
            this.imagePath = null;
        }
        this.isPublic = isPublic;
    }
}
