package us.usserver.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.base.BaseEntity;
import us.usserver.global.oauth.oauthEnum.SocialType;
import us.usserver.member.memberEnum.Gender;
import us.usserver.member.memberEnum.Role;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotBlank
    private String socialId;

    @NotBlank
    private SocialType socialType;

    @NotBlank
    private String email;

    @NotNull
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotBlank
    private Boolean isAdult;
}
