package us.usserver.domain.novel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoreInfoOfNovel {
    @Schema(description = "마지막 소설 Id", nullable = true, example = "5")
    Long lastNovelId;
}
