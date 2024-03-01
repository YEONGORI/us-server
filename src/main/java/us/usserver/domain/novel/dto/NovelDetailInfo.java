package us.usserver.domain.novel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import us.usserver.domain.chapter.dto.ChapterInfo;
import us.usserver.domain.novel.constant.AgeRating;
import us.usserver.domain.novel.constant.Genre;
import us.usserver.domain.novel.constant.Hashtag;
import us.usserver.domain.authority.dto.StakeInfo;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NovelDetailInfo {
    @Schema(description = "소설 제목", example = "주술 회전")
    private String title;

    @Schema(description = "소설 썸네일", nullable = true, example = "https:// ~")
    private String thumbnail;

    @Schema(description = "소설 줄거리",example = "옛날 옛날 아주 머언 옛낫...")
    private String synopsis;

    @Schema(description = "메인 작가 이름",example = "강아지상")
    private String authorName;

    @Schema(description = "메인 작가 소개",example = "안녕하세요. 메인자갑니다.")
    private String authorIntroduction;

    @Schema(description = "이용가",example = "GENERAL, TWELVE_PLUS, FIFTEEN_PLUS,")
    private AgeRating ageRating;

    @Schema(description = "소설 장르", example = "FANTASY")
    private Genre genre;

    @Schema(description = "해시태그",example = "[HASHTAG1, HASHTAG2, ...]")
    private Set<Hashtag> hashtags;

    @Schema(description = "지분 정보", nullable = true, example = "[{}, {}, ...]")
    private List<StakeInfo> stakeInfos;

    @Schema(description = "회차 정보", example = "[{}, {}, ...]")
    private List<ChapterInfo> chapterInfos;
}
