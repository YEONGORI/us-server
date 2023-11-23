package us.usserver.paragraph.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphSelected {
    private Long id;
    private String content;
    private int order;
}
