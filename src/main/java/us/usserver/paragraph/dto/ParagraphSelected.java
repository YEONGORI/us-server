package us.usserver.paragraph.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.paragraph.Paragraph;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphSelected {
    private Long id;
    private String content;
    private Long authorId;
    private int sequence;

    public static ParagraphSelected fromParagraph(Paragraph paragraph) {
        return ParagraphSelected.builder()
                .id(paragraph.getId())
                .content(paragraph.getContent())
                .authorId(paragraph.getAuthor().getId())
                .sequence(paragraph.getSequence())
                .build();
    }
}
