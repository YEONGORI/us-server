package us.usserver.novel.dto;

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

import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNovelReq {
    @Size(max = 16, min = 2)
    @NotBlank
    String title;
    @Size(max = 300)
    @NotBlank
    String synopsis;
    @Size(max = 300)
    @NotBlank
    String authorDescription;
    @Size(max = 8)
    @NotNull
    Set<Hashtag> hashtag;
    @NotNull
    Genre genre;
    @NotNull
    AgeRating ageRating;
    @NotNull
    NovelSize novelSize;
    @NotBlank
    String thumbnail;

    public Novel toEntity(Author author) {
        return Novel.builder()
                .title(title)
                .thumbnail(thumbnail)
                .synopsis(synopsis)
                .authorDescription(authorDescription)
                .hashtag(hashtag)
                .genre(genre)
                .ageRating(ageRating)
                .hit(0)
                .author(author)
                .status(NovelStatus.IN_PROGRESS)
                .build();
    }
}
