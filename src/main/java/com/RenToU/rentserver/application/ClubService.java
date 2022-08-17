package com.RenToU.rentserver.application;

import com.RenToU.rentserver.DTO.ClubDTO;
import com.RenToU.rentserver.DTO.NotificationDTO;
import com.RenToU.rentserver.DTO.ProductDTO;
import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Notification;

import java.util.List;

public interface ClubService {
    public List<Club> findClubs();

    public ClubDTO createClub(Long memberId, String clubName, String clubIntro, String thumbnailPath);

    public void requestClubJoin(Long clubId, Long memberId);
    public void acceptClubJoin(Long clubId, Long ownerId, Long joinMemberId);

    public NotificationDTO createNotification(Long clubId, Long WriterId, NotificationDTO notificationDTO);
    public void registerProduct(Long clubId, ProductDTO productDTO, Long memberId);
    public void registerItem(Long productId, Long memberId);
}
