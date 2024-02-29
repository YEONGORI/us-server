package us.usserver.domain.paragraph.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostParagraphReq {
    @Schema(description = "한줄 내용", example = "제가 1994년에 LA에 있었을 때... 이러쿵 저러쿵")
    private String content;
}
