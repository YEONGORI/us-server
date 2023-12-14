package us.usserver.novel.dto;

import lombok.*;
import us.usserver.chapter.dto.ChapterInfo;
import us.usserver.novel.Novel;
import us.usserver.novel.novelEnum.AgeRating;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;
import us.usserver.stake.dto.StakeInfo;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NovelDetailInfo {
    private String title;
    private String thumbnail;
    private String synopsis;
    private String authorName;
    private String authorIntroduction;
    private AgeRating ageRating;
    private Genre genre;
    private Set<Hashtag> hashtags;
    private List<StakeInfo> stakeInfos;
    private List<ChapterInfo> chapterInfos;
}
