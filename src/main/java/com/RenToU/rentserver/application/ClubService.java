package com.RenToU.rentserver.application;

import com.RenToU.rentserver.DTO.NotificationDTO;
import com.RenToU.rentserver.DTO.ProductDTO;
import com.RenToU.rentserver.domain.Club;

import java.util.List;

public interface ClubService {
    public List<Club> findClubs();

    public Long createClub(Long memberId, String clubName, String clubIntro);

    public void requestClubJoin(Long clubId, Long memberId);
    public void acceptClubJoin(Long clubId, Long ownerId, Long joinMemberId);

    public Long createNotification(Long clubId, Long WriterId, NotificationDTO notificationDTO);
    public void registerProduct(Long clubId, ProductDTO productDTO, Long memberId);
    public void registerItem(Long productId, Long memberId);
}
