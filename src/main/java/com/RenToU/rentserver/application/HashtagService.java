package com.RenToU.rentserver.application;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.ClubHashtag;
import com.RenToU.rentserver.domain.Hashtag;
import com.RenToU.rentserver.exceptions.ClubErrorCode;
import com.RenToU.rentserver.exceptions.CustomException;
import com.RenToU.rentserver.infrastructure.ClubHashtagRepository;
import com.RenToU.rentserver.infrastructure.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;
    private final ClubHashtagRepository clubHashtagRepository;

    public List<Club> findClubsWithHashtag(String hashtagName) {
        try {
            Hashtag hashtag = findHashtagByName(hashtagName);

            List<ClubHashtag> clubHashtags = clubHashtagRepository.findByHashtag(hashtag);
            List<Club> clubs = clubHashtags.stream().map(clubHashtag -> {
                return clubHashtag.getClub();
            }).collect(Collectors.toList());
            return clubs;
        } catch (CustomException e) {
            return new ArrayList<Club>();
        }
    }

    private Hashtag findHashtagByName(String hashtagName) {
        return hashtagRepository.findByName(hashtagName)
                .orElseThrow(() -> new CustomException(ClubErrorCode.HASHTAG_NOT_FOUND));
    }
}
