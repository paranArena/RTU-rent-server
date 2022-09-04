package com.RenToU.rentserver.dto.response;

import com.RenToU.rentserver.domain.Location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private String name;
    private Double latitude;
    private Double longitude;

    public static LocationDto from(Location location){
        if(location == null) return null;

        return LocationDto.builder()
        .name(location.getName())
        .latitude(location.getX())
        .longitude(location.getY())
        .build();
    }
}
