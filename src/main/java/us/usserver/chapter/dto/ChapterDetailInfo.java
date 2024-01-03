package us.usserver.chapter.dto;

import lombok.*;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.paragraph.dto.ParagraphInVoting;
import us.usserver.paragraph.dto.ParagraphSelected;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDetailInfo {
    private int part;
    private String title;
    private ChapterStatus status;
    private Double score;
    private List<ParagraphSelected> selectedParagraphs;
    private ParagraphInVoting myParagraph;
    private ParagraphInVoting bestParagraph;
    private String prevChapterUrl;
    private String nextChapterUrl;
}
