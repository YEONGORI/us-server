package us.usserver.novel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import us.usserver.novel.novelEnum.Sorts;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovelPageInfoResponse {
    private Long lastNovelId;
    private Boolean hasNext;
    private Sorts sorts;
}
