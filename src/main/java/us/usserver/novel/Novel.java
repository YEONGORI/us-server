package us.usserver.novel;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.base.BaseEntity;
import us.usserver.chapter.Chapter;
import us.usserver.novel.novelEnum.AgeRating;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @Size(max = 30) // Length(max=30)으로 설정 하면 한글은 10자 까지 입력 가능
    private String title;

    @NotBlank
    @Lob
    private String thumbnail;

    @NotBlank
    @Size(max = 500)
    private String author; // 종두 님의 Author class 로 교체 예정

    @NotBlank
    @Size(max = 500)
    private String synopsis;

    @NotBlank
    @Enumerated(EnumType.STRING) // Enum 순서가 자주 변할 예정 이므로 String 으로 저장
    private Set<Hashtag> hashtag;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Set<Genre> genre;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Set<AgeRating> ageRating;

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<Chapter> chapters = new ArrayList<>();
}
