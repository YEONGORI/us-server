package us.usserver.chapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import us.usserver.paragraph.dto.ParagraphInfo;

import java.util.List;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDetailInfo {
    // TODO  챕터 디테일 조회하면 모든 파라그래프랑 다 주는 기능 만들고 있었음
    // 사실 파라그래프 패키지에 있는 기능인데 바꾸는 중
    private String chapterTitle;
    private 
    private List<ParagraphInfo> paragraphInfos;
}
