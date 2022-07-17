package com.RenToU.rentserver.DTO;

import com.RenToU.rentserver.domain.ClubMember;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long id;
    @NotBlank
    @Mapping("email")
    private String email;
    @NotBlank
    @Mapping("password")
    private String password;
    @NotBlank
    @Mapping("name")
    private String name;
    @NotBlank
    @Mapping("phoneNumber")
    private String phoneNumber;
    @NotBlank
    @Mapping("studentId")
    private String studentId;
    @NotBlank
    @Mapping("major")
    private String major;

    @OneToMany(mappedBy = "member")
    private List<ClubMember> clubList = new ArrayList<>();

}
