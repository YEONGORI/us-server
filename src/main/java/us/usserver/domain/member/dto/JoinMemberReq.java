package us.usserver.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import us.usserver.global.oauth.oauthEnum.OauthProvider;
import us.usserver.domain.member.constant.Gender;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinMemberReq {
    @Schema(description = "사용자 소셜 ID", example = "3123123")
    @NotBlank
    private String socialId;
    @Schema(description = "사용자 소셜 Type", example = "KAKAO, GOOGLE, NAVER")
    @NotBlank
    @Enumerated(EnumType.STRING)
    private OauthProvider oauthProvider;
    @Schema(description = "사용자 이메일", example = "aorkserni123@naver.com")
    @NotBlank
    private String email;
    @Schema(description = "사용자 성별", example = "FEMALE")
    @NotBlank
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Schema(description = "사용자 생일", example = "2023-10-11 12:45")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotBlank
    private LocalDateTime birthday;
}
