package us.usserver.chapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import us.usserver.paragraph.dto.ParagraphInVoting;
import us.usserver.paragraph.dto.ParagraphSelected;

import java.util.List;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDetailInfo {
    private int part;
    private String title;
    private List<ParagraphSelected> selectedParagraphs;
    private ParagraphInVoting myParagraph;
    private ParagraphInVoting bestParagraph;
    private String prevChapterUrl;
    private String nextChapterUrl;
}
