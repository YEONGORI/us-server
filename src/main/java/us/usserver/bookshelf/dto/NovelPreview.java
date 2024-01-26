package us.usserver.bookshelf.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import us.usserver.author.dto.AuthorInfo;
import us.usserver.novel.Novel;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NovelPreview {
    @Schema(description = "조회된 소설 제목", nullable = false, example = "주술회전")
    private String title;

    @Schema(description = "소설 메인 작가 이름", nullable = false, example = "냐옹씨")
    private AuthorInfo mainAuthor;

    @Schema(description = "참여중인 작가들 수", nullable = false, example = "27")
    private Integer joinedAuthor;

    @Schema(description = "소설 썸네일", nullable = false, example = "https://something-special-thumbnail.com/1")
    private String thumbnail;

    @Schema(description = "바로가기", nullable = false, example = "https://some-url/novel/novelId")
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
