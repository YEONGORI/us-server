package us.usserver.novel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchKeywordResponse {
    @Schema(description = "최근 검색어", example = "남주, 주술회전, ...")
    List<String> recentSearch;
    @Schema(description = "인기 검색어", example = "1.먼치킨 소설, 2.회귀소설, 3. ...")
    List<String> hotSearch;
}
