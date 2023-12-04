package us.usserver.novel;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import us.usserver.author.Author;
import us.usserver.base.BaseEntity;
import us.usserver.chapter.Chapter;
import us.usserver.comment.novel.NoComment;
import us.usserver.novel.novelEnum.*;
import us.usserver.stake.Stake;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Novel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "novel_id")
    private Long id;

    @NotBlank
    @Size(max = 16) // Length(max=30)으로 설정 하면 한글은 10자 까지 입력 가능
    private String title;

    @NotBlank
    private String thumbnail;

    @NotBlank
    @Size(max = 300)
    private String synopsis;

    @NotBlank
    @Size(max = 300)
    private String authorDescription;

//    @NotNull
    @Enumerated(EnumType.STRING) // Enum 순서가 자주 변할 예정 이므로 String 으로 저장
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Hashtag> hashtags;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AgeRating ageRating;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NovelStatus novelStatus;
    @NotNull
    private Integer hit;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NovelSize novelSize;

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<Chapter> chapters = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<Stake> stakes = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<NoComment> noComments = new ArrayList<>();
}
