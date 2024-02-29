package us.usserver.global.oauth.dto;

import us.usserver.domain.member.constant.Role;

public record MemberInfoDto(
        Long userId,
        Role role,
        String accessToken,
        String refreshToken
) {
}
