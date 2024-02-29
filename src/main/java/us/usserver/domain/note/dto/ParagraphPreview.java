package us.usserver.domain.note.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.novel.Novel;
import us.usserver.domain.paragraph.entity.Paragraph;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphPreview {
    @Schema(description = "작성한 작가 식별 id", example = "12")
    private Long authorId;

    @Schema(description = "소설 제목", example = "주술회전")
    private String novelTitle;

    @Schema(description = "회차 제목", example = "주술회전 2화")
    private String chapterTitle;

    @Schema(description = "한줄 내용", example = "제가 LA에 있을때... 주저리 주저리")
    private String paragraphContent;

    @Schema(description = "소설 썸네일", example = "https://thumbnail-url.com/2")
    private String thumbnail;

    @Schema(description = "메인 바로가기", example = "https://someurl.com/novel/2")
    private String shortcuts;

    @Schema(description = "한줄 작성 날짜", example = "2023.12.31 00:00:00")
    private LocalDateTime date;

    public static ParagraphPreview fromParagraph(Paragraph paragraph, Novel novel, Chapter chapter) {
        return ParagraphPreview.builder()
                .authorId(paragraph.getAuthor().getId())
                .novelTitle(novel.getTitle())
                .chapterTitle(chapter.getTitle())
                .paragraphContent(paragraph.getContent())
                .thumbnail(novel.getThumbnail())
                .shortcuts(getShortcuts(novel.getId(), chapter.getId()))
                .date(paragraph.getCreatedAt())
                .build();
    }

    private static String getShortcuts(Long novelId, Long chapterId) { // TODO: 이후 URL에 따라 수정
        return "http://localhost:8080/" + novelId + "/" + chapterId;
    }
}
