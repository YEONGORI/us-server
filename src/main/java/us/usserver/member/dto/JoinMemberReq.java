package us.usserver.member.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.global.oauth.oauthEnum.SocialType;
import us.usserver.member.memberEnum.Gender;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinMemberReq {
    @NotBlank
    private String socialId;
    @NotBlank
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    @NotBlank
    private String email;
    @NotBlank
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @NotBlank
    private LocalDateTime birthday;
    @NotBlank
    private String phoneNumber;
}
