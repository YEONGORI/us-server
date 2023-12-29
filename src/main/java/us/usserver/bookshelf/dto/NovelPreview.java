package us.usserver.bookshelf.dto;

import lombok.*;
import us.usserver.author.dto.AuthorInfo;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NovelPreview {
    private String title;
    private AuthorInfo mainAuthor;
    private Integer joinedAuthor;
    private String thumbnail;
    private String shortcuts;
}
