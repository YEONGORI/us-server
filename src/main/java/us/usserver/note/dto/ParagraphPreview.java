package us.usserver.note.dto;

import lombok.*;
import us.usserver.chapter.Chapter;
import us.usserver.novel.Novel;
import us.usserver.paragraph.Paragraph;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphPreview {
    private String novelTitle;
    private String chapterTitle;
    private String paragraphContent;
    private String thumbnail;
    private String shortcuts;
    private LocalDateTime date;

    public static ParagraphPreview fromParagraph(Paragraph paragraph, Novel novel, Chapter chapter) {
        return ParagraphPreview.builder()
                .novelTitle(novel.getTitle())
                .chapterTitle(chapter.getTitle())
                .paragraphContent(paragraph.getContent())
                .thumbnail(novel.getThumbnail())
                .shortcuts(getShortcuts(novel.getId(), chapter.getId()))
                .build();
    }

    private static String getShortcuts(Long novelId, Long chapterId) { // TODO: 이후 URL에 따라 수정
        return "http://localhost:8080/" + novelId + "/" + chapterId;
    }
}
