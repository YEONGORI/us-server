package us.usserver.novel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.author.Author;
import us.usserver.novel.Novel;
import us.usserver.novel.novelEnum.AgeRating;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;
import us.usserver.novel.novelEnum.NovelStatus;
import us.usserver.novel.novelEnum.NovelSize;

import java.util.ArrayList;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNovelReq {
    @Schema(description = "소설 제목", example = "주술회전")
    @Size(max = 16, min = 2)
    @NotBlank
    String title;
    @Schema(description = "소설 줄거리", example = "주술을 사용하며 싸우는 액션소설")
    @Size(max = 300)
    @NotBlank
    String synopsis;
    @Schema(description = "작가 소개", example = "액선과 판타지를 연재하고 있는 xxx입니다.")
    @Size(max = 300)
    @NotBlank
    String authorDescription;
    @Schema(description = "소설을 나타내는 태그", example = "#먼치킨, #병맛")
    @Size(max = 8)
    @NotNull
    Set<Hashtag> hashtag;
    @Schema(description = "소설 장르", example = "액션")
    @NotNull
    Genre genre;
    @Schema(description = "소설 연령 제한", example = "19세 이용가")
    @NotNull
    AgeRating ageRating;
    @Schema(description = "소설 크기 분류", example = "장편소설")
    @NotNull
    NovelSize novelSize;
    @Schema(description = "소설 썸네일", example = "주술회전.jpg")
    @NotBlank
    String thumbnail;

    public Novel toEntity(Author author) {
        return Novel.builder()
                .title(title)
                .thumbnail(thumbnail)
                .synopsis(synopsis)
                .authorDescription(authorDescription)
                .mainAuthor(author)
                .hashtags(hashtag)
                .genre(genre)
                .chapters(new ArrayList<>())
                .ageRating(ageRating)
                .novelSize(novelSize)
                .hit(0)
                .novelStatus(NovelStatus.IN_PROGRESS)
                .build();
    }
}
