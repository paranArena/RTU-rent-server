package com.RenToU.rentserver.infrastructure.clubMember;

import com.RenToU.rentserver.domain.ClubMember;
import com.RenToU.rentserver.domain.ClubRole;
import com.RenToU.rentserver.domain.QClubMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.RenToU.rentserver.domain.QClub.club;
import static com.RenToU.rentserver.domain.QClubMember.clubMember;
@RequiredArgsConstructor
public class ClubMemberRepositoryImpl implements ClubMemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ClubMember> searchByMemberIdWithClub(long memberId) {

        return queryFactory
                .selectFrom(clubMember)
                .innerJoin(clubMember.club, club)
                .where(clubMember.role.ne(ClubRole.WAIT))
                .fetchJoin()
                .distinct()
                .where(
                    memberIdEq(memberId)
                )
                .fetch();
    }

    private BooleanBuilder memberIdEq(Long memberId){
        if(memberId != null){
            return new BooleanBuilder(clubMember.member.id.eq(memberId));
        }
        return new BooleanBuilder();
    }
}
