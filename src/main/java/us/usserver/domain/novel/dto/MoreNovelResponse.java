package us.usserver.domain.novel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.domain.novel.constant.Sorts;

import java.util.List;

@Builder
public record MoreNovelResponse(
        @Schema(description = "더보기 분류에 따른 소설 List", example = "novel1, novel2, ...")
        List<NovelInfo> novelList,
        @Schema(description = "소설 정렬", example = "NEW, DESC")
        Integer currentPage,
        @Schema(description = "마지막 게시물 여부", example = "true")
        Boolean hasNext
) {}
