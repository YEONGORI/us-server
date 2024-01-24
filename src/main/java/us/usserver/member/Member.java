package us.usserver.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import us.usserver.base.BaseEntity;
import us.usserver.member.memberEnum.Gender;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
