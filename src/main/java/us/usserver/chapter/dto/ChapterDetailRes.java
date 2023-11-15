package us.usserver.chapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import us.usserver.paragraph.dto.ParagraphInfo;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDetailRes {
    private Long id;
    private String title;
    private Integer part;

    private List<ParagraphInfo> paragraphInfos;
}
