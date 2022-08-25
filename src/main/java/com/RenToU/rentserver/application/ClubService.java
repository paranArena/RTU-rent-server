package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.dto.request.CreateNotificationDto;
import com.RenToU.rentserver.dto.request.CreateProductDto;
import com.RenToU.rentserver.dto.service.ProductServiceDto;

import java.util.List;

public interface ClubService {
    public List<Club> findClubs();

    public Club createClub(Long memberId, String clubName, String clubIntro, String thumbnailPath, List<String> clubHashtags);

    public void requestClubJoin(Long clubId, Long memberId);
    public void acceptClubJoin(Long clubId, Long ownerId, Long joinMemberId);

    public void createNotification(Long clubId, Long WriterId, CreateNotificationDto notificationDto);
    public void registerProduct(ProductServiceDto productDto);
    public void registerItem(Long productId, Long memberId);
}
