package us.usserver.novel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import us.usserver.novel.Novel;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HomeNovelListResponse {
    @Schema(description = "실시간 업데이트 소설 List", example = "novel1, novel2, ...")
    private List<Novel> realTimeNovels;
    @Schema(description = "신작 소설 List", example = "novel1, novel2, ...")
    private List<Novel> newNovels;
    @Schema(description = "내가 읽은 소설 List", example = "novel1, novel2, ...")
    private List<Novel> readNovels;
}
