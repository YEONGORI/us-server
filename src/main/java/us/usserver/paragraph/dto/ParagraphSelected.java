package us.usserver.paragraph.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "한줄(paragraph) 식별 id", nullable = false, example = "234")
    private Long id;

    @Schema(description = "한줄 내용", nullable = false, example = "제가 LA에 있을 때... 주저리 주저리 궁시렁 궁시렁...")
    private String content;

    @Schema(description = "작성한 작가 식별 id", nullable = false, example = "123")
    private Long authorId;

    @Schema(description = "한 회차에서 몇 번째로 등장하는 한줄인지에 대한 인덱스", nullable = false, example = "2")
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
