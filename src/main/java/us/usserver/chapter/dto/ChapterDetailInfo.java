package us.usserver.chapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "소설 내 회차 수", example = "1화, 2화, 3화 ...")
    private int part;

    @Schema(description = "회차 제목", example = "주술회전 1화")
    private String title;

    @Schema(description = "회차 정보", example = "작성중(IN_PROGRESS), 작성완료(COMPLETED)")
    private ChapterStatus status;

    @Schema(description = "회차 평점", example = "4.1, 4.9, ...")
    private Double score;

    @Schema(description = "선정 된 한줄들", example = "")
    private List<ParagraphSelected> selectedParagraphs;

    @Schema(description = "투표중인 내가 쓴 한줄", example = "{id: 111, ...}")
    private ParagraphInVoting myParagraph;

    @Schema(description = "가장 투표를 많이 받은 한줄", example = "{id: 111, ...}")
    private ParagraphInVoting bestParagraph;

    @Schema(description = "이전 회차 바로가기", example = "https://some-url/chapter/1/1")
    private String prevChapterUrl;

    @Schema(description = "다음 회차 바로가기", example = "https://some-url/chapter/1/3")
    private String nextChapterUrl;
}
