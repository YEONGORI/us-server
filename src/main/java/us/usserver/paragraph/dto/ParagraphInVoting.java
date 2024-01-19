package us.usserver.paragraph.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.paragraph.Paragraph;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphInVoting {
    @Schema(description = "한줄(paragraph) 식별 id", nullable = false, example = "234")
    private Long id;

    @Schema(description = "한줄 내용", nullable = false, example = "제가 LA에 있을 때... 주저리 주저리 궁시렁 궁시렁...")
    private String content;

    @Schema(description = "한 회차에서 몇 번째로 등장하는 한줄인지에 대한 인덱스", nullable = false, example = "2")
    private int sequence;

    @Schema(description = "총 좋아요 갯수", nullable = false, example = "222")
    private int likeCnt;

    @Schema(description = "한줄 상태", nullable = false, example = "투표중(IN_VOTING), 선정(SELECTED), 미선정(UNSELECTED)")
    private ParagraphStatus status;

    @Schema(description = "작성한 작가 식별 id", nullable = false, example = "123")
    private Long authorId;

    @Schema(description = "작성한 작가 닉네임", nullable = false, example = "고양이호두")
    private String authorName;

    @Schema(description = "생성 날짜", nullable = false, example = "2023.12.31. 00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "업데이트 날짜", nullable = false, example = "2023.12.31. 00:00:00")
    private LocalDateTime updatedAt;

    public static ParagraphInVoting fromParagraph(Paragraph paragraph, int likeCnt) {
        return ParagraphInVoting.builder()
                .id(paragraph.getId())
                .content(paragraph.getContent())
                .sequence(paragraph.getSequence())
                .likeCnt(likeCnt)
                .status(paragraph.getParagraphStatus())
                .authorId(paragraph.getAuthor().getId())
                .authorName(paragraph.getAuthor().getNickname())
                .build();
    }
}
