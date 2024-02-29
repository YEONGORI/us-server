package us.usserver.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import us.usserver.domain.member.entity.Author;

@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AuthorInfo {
    @Schema(description = "작가 식별 id", example = "333")
    private Long id;

    @Schema(description = "작가 닉네임", example = "특별한닉네임")
    private String nickname;

    public static AuthorInfo fromAuthor(Author author) {
        return AuthorInfo.builder()
                .id(author.getId())
                .nickname(author.getNickname())
                .build();
    }
}
