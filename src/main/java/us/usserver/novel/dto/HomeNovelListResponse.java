package us.usserver.novel.dto;

import lombok.*;
import us.usserver.novel.Novel;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HomeNovelListResponse {
    private List<Novel> realTimeNovels;
    private List<Novel> newNovels;
    private List<Novel> readNovels;
}
