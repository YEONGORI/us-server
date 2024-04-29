package us.usserver.domain.paragraph.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record PostParagraphReq(
        @Min(50) @Max(300)
        @Schema(description = "한줄 내용", example = "제가 1994년에 LA에 있었을 때... 이러쿵 저러쿵")
        String content
) {}
