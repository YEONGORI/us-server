package us.usserver.domain.novel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record MoreNovelRequest(
        @Schema(description = "다음 소설 페이지", nullable = true, example = "더보기 request의 response에 보면 page가 적혀 있음 그 값 전달", defaultValue = "0")
        Integer nextPage,
        @Schema(description = "더보기 탭 종류", nullable = true, example = "실시간 업데이트 or 인기 소설 or 따끈따끈한 신작 or 읽은 소설")
        MainNovelType mainNovelType
) {}
