package us.usserver.domain.novel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.domain.novel.constant.Sorts;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovelPageInfoResponse {
    @Schema(description = "더보기 분류에 따른 소설 List", example = "novel1, novel2, ...")
    private List<NovelInfo> novelList;
    @Schema(description = "마지막 소설 ID", example = "5")
    private Long lastNovelId;
    @Schema(description = "마지막 게시물 여부", example = "true")
    private Boolean hasNext;
    @Schema(description = "소설 정렬", example = "NEW, DESC")
    private Sorts sorts;
}
