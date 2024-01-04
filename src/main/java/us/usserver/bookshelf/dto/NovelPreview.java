package us.usserver.bookshelf.dto;

import lombok.*;
import us.usserver.author.dto.AuthorInfo;
import us.usserver.novel.Novel;

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

    public static NovelPreview fromNovel(Novel novel, Integer joinedAuthor, String shortcuts) {
        return NovelPreview.builder()
                .title(novel.getTitle())
                .mainAuthor(AuthorInfo.fromAuthor(novel.getMainAuthor()))
                .joinedAuthor(joinedAuthor)
                .thumbnail(novel.getThumbnail())
                .shortcuts(shortcuts)
                .build();
    }
}
