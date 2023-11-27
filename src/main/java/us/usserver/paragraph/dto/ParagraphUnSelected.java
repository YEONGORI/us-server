package us.usserver.paragraph.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphUnSelected {
    private Long id;
    private String novelName;
    private String chapterName;
    private String content;
    private int likeCnt;
}
