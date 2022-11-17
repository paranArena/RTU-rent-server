package com.RenToU.rentserver.dto.response.preview;

import com.RenToU.rentserver.domain.CouponMember;
import com.RenToU.rentserver.domain.CouponMemberHistory;

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
public class CouponMemberPreviewDto {

    private Long id;

    private MemberPreviewDto memberPreviewDto;

    public static CouponMemberPreviewDto from(CouponMember cm) {
        if (cm == null)
            return null;

        return CouponMemberPreviewDto.builder()
                .id(cm.getId())
                .memberPreviewDto(MemberPreviewDto.from(cm.getMember()))
                .build();
    }

    public static CouponMemberPreviewDto from(CouponMemberHistory cm) {
        if (cm == null)
            return null;

        return CouponMemberPreviewDto.builder()
                .id(cm.getId())
                .memberPreviewDto(MemberPreviewDto.from(cm.getMember()))
                .build();
    }
}
