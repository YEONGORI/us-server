package us.usserver.paragraph.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.author.dto.AuthorInfo;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphInfo {
    private Long id;
    private String content;
    private int order;
    private int likeCnt;
    private ParagraphStatus status;
    private Long authorId;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ParagraphInfo fromParagraph(Paragraph paragraph, int likeCnt) {
        return ParagraphInfo.builder()
                .id(paragraph.getId())
                .content(paragraph.getContent())
                .order(paragraph.getOrder())
                .likeCnt(likeCnt)
                .status(paragraph.getParagraphStatus())
                .authorId(paragraph.getAuthor().getId())
                .authorName(paragraph.getAuthor().getNickname())
                .build();
    }
}
