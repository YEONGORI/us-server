package us.usserver.author.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.author.Author;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorInfo {
    private Long id;
    private String nickName;

    public static AuthorInfo fromAuthor(Author author) {
        return AuthorInfo.builder()
                .id(author.getId())
                .nickName(author.getNickname())
                .build();
    }
}
