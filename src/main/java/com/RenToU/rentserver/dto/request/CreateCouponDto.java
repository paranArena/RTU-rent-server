package com.RenToU.rentserver.dto.request;

import com.RenToU.rentserver.domain.Location;
import com.RenToU.rentserver.domain.RentalPolicy;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCouponDto {

    private String name;

    private String locationName;

    private Double latitude;

    private Double longitude;

    private String information;

    private String imagePath;

    private String actDate;

    private String expDate;
}
