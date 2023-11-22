package us.usserver.paragraph.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphInfo {
    private Long id;
    private String content;
    private int number;
    private ParagraphStatus paragraphStatus;

}
