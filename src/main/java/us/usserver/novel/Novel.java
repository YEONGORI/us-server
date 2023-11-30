package us.usserver.novel;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import us.usserver.author.Author;
import us.usserver.authority.Authority;
import us.usserver.base.BaseEntity;
import us.usserver.chapter.Chapter;
import us.usserver.comment.novel.NoComment;
import us.usserver.novel.novelEnum.AgeRating;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;
import us.usserver.stake.Stake;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "novel_id")
    private Long id;

    @NotBlank
    @Size(max = 16) // Length(max=30)으로 설정 하면 한글은 10자 까지 입력 가능
    private String title;

    @Setter
    @NotBlank
    @Size(max = 500)
    private String thumbnail;

    @Setter
    @NotBlank
    @Size(max = 300)
    private String synopsis;

    @Setter
    @NotBlank
    @Size(max = 300)
    @Column(name = "authordescription")
    private String authorDescription;

    @NotNull
    @Enumerated(EnumType.STRING) // Enum 순서가 자주 변할 예정 이므로 String 으로 저장
//    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Hashtag> hashtag;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AgeRating ageRating;

    @OneToOne
    @JoinColumn(name = "author_id")
    private Author mainAuthor;

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<Chapter> chapters = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<Stake> stakes = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<NoComment> noComments = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<Authority> authorities = new ArrayList<>();
}
