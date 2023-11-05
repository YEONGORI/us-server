package us.usserver.novel;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.base.BaseEntity;
import us.usserver.novel.novelEnum.AgeRating;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Novel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 30) // Length(max=30)으로 설정하면 한글은 10자 까지 입력 가능
    private String title;

    @NotBlank
    private String thumbnail;

    @NotBlank
    @Size(max = 500)
    private String author; // 종두님의 Author class로 교체 예정

    @NotBlank
    @Size(max = 500)
    private String synopsis;

    @NotBlank
    private Set<Hashtag> hashtag;

    @NotBlank
    private Set<Genre> genre;

    @NotBlank
    private Set<AgeRating> ageRating;
}
