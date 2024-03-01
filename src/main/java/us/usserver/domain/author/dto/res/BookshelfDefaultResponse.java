package us.usserver.domain.author.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.domain.author.dto.NovelPreview;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookshelfDefaultResponse {
    @Schema(description = "더보기 분류에 따른 소설 List", example = "소설미리보기1, 소설미리보기2, 소설미리보기3, ...")
    private List<NovelPreview> novelPreviews;
}
