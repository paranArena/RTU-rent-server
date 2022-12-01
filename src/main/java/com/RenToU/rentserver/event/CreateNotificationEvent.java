package com.RenToU.rentserver.event;

import com.RenToU.rentserver.domain.Club;
import com.RenToU.rentserver.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CreateNotificationEvent {
    private final Club club;
    private final List<Member> MemberList;
}
