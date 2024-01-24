package us.usserver.novel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "소설 제목", example = "주술 회전")
    private String title;

    @Schema(description = "소설 장르", example = "FANTASY")
    private Genre genre;

    @Schema(description = "해시태그",example = "[HASHTAG1, HASHTAG2, ...]")
    private Set<Hashtag> hashtag;

    @Schema(description = "메인 작가 이름",example = "강아지상")
    private Author createdAuthor;

    @Schema(description = "참여 작가 수",example = "12")
    private Integer joinedAuthorCnt;

    @Schema(description = "댓글 갯수",example = "899")
    private Integer commentCnt;

    @Schema(description = "좋아요 갯수",example = "998")
    private Integer likeCnt;

    @Schema(description = "소설 공유 url",example = "https:// ~ ~")
    private String novelSharelUrl;
}
