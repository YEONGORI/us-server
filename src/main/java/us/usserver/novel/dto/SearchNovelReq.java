package us.usserver.novel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.novel.novelEnum.Hashtag;
import us.usserver.novel.novelEnum.NovelStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchNovelReq {
        //security 도입 시 삭제
        Long authorId;
        @Schema(description = "소설 제목 검색어", example = "주술")
        String title;
        @Schema(description = "소설 해시태그 필터", example = "#먼치킨")
        Hashtag hashtag;
        @Schema(description = "소설 상태 필터", example = "COMPLETED")
        NovelStatus status;
        @Schema(description = "마지막 소설 ID", example = "5")
        @NotNull
        Long lastNovelId;
        @Schema(description = "불러올 소설 개수", example = "6")
        Integer size;
        @Schema(description = "소설 정렬", example = "NEW, DESC")
        SortDto sortDto;
}