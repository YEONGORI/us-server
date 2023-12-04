package us.usserver.novel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.novel.Novel;
import us.usserver.novel.novelEnum.Sorts;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovelPageInfoResponse {
    private List<Novel> novelList;
    private Long lastNovelId;
    private Boolean hasNext;
    private Sorts sorts;
}
