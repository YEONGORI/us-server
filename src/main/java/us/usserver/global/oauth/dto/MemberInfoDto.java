package us.usserver.global.oauth.dto;

import us.usserver.member.memberEnum.Role;

public record MemberInfoDto(
        Long userId,
        Role role,
        String accessToken,
        String refreshToken
) {
}
