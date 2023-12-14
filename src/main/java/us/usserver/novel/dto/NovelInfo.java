package us.usserver.novel.dto;

import lombok.*;
import us.usserver.author.Author;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;

import java.util.Set;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NovelInfo {
    private String title;
    private Genre genre;
    private Set<Hashtag> hashtag;
    private Author createdAuthor;
    private Integer joinedAuthorCnt;
    private Integer commentCnt;
    private String novelSharelUrl;
}
