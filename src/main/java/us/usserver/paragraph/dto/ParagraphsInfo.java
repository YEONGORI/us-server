package us.usserver.paragraph.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphsInfo {
    private List<ParagraphSelected> selectedParagraphs;
    private ParagraphInfo myParagraph;
    private ParagraphInfo bestParagraph;
}
