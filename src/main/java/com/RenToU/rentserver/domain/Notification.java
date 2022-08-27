package com.RenToU.rentserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Getter @Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Notification extends BaseTimeEntity{
    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    private String title;

    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    public static Notification createNotification(String title, String content,Member writer,Club club) {
         Notification notification =  Notification.builder()
                 .title(title)
                 .content(content)
                 .writer(writer)
                 .build();
         club.addNotification(notification);
         notification.setClub(club);
         return notification;
    }
}
