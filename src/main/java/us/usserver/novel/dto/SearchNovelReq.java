package us.usserver.novel.dto;

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
        String title;
        Hashtag hashtag;
        NovelStatus status;
        Long lastNovelId;
        Integer size;
        SortDto sortDto;
}