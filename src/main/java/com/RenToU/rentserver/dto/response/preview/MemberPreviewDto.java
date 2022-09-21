package com.RenToU.rentserver.dto.response.preview;

import com.RenToU.rentserver.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPreviewDto {
    private Long id;
    private String name;
    private String major;
    private String studentId;

    public static MemberPreviewDto from(Member member) {
        return MemberPreviewDto.builder()
                .id(member.getId())
                .name(member.getName())
                .major(member.getMajor())
                .studentId(member.getStudentId())
                .build();
    }
}
