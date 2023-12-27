package us.usserver.novel;


import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "소설 ID", nullable = true, example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "novel_id")
    private Long id;

    @Schema(description = "소설 제목", nullable = true, example = "주술회전")
    @NotBlank
    @Size(max = 16) // Length(max=30)으로 설정 하면 한글은 10자 까지 입력 가능
    private String title;

    @Setter
    @Schema(description = "소설 썸네일", nullable = true, example = "주술회전.jpg")
    @NotBlank
    @Size(max = 500)
    private String thumbnail;

    @Setter
    @Schema(description = "소설 줄거리", nullable = true, example = "주술을 쓰면서 싸우는 웹소설")
    @NotBlank
    @Size(max = 300)
    private String synopsis;

    @Setter
    @Schema(description = "작가 소개", nullable = true, example = "액션 소설을 좋아하는 작가입니다.")
    @NotBlank
    @Size(max = 300)
    private String authorDescription;

    @Schema(description = "소설 해시태그", example = "MONCHKIN, HASHTAG1, ...")
//    @NotNull
    @Enumerated(EnumType.STRING) // Enum 순서가 자주 변할 예정 이므로 String 으로 저장
//    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Hashtag> hashtag;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Hashtag> hashtags;

    @Schema(description = "소설 장르", nullable = true, example = "FANTASY")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Schema(description = "소설 연령대", nullable = true, example = "FIFTEEN_PLUS")
    @NotNull
    @Enumerated(EnumType.STRING)
    private AgeRating ageRating;

    @Schema(description = "소설 연재 상태", nullable = true, example = "IN_PROGRESS")
    @NotNull
    @Enumerated(EnumType.STRING)
    private NovelStatus novelStatus;

    @Schema(description = "조회수", nullable = true, example = "100")
    @NotNull
    private Integer hit;

    @Schema(description = "작가", nullable = true, example = "author1")
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author mainAuthor;

    @Schema(description = "작가", nullable = true, example = "author1")
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author; log



    @Schema(description = "소설 분류", nullable = true, example = "장편소설")
    @NotNull
    @Enumerated(EnumType.STRING)
    private NovelSize novelSize;

    @Schema(description = "소설 회차 List", example = "Chapter_01, Chapter_02, ...")
    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<Chapter> chapters = new ArrayList<>();

    @Schema(description = "소설 지분 List", example = "Stake_01, Stake_02, ...")
    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<Stake> stakes = new ArrayList<>();

    @Schema(description = "소설 댓글 List", example = "Comment_01, Comment_02, ...")
    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<NoComment> noComments = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL)
    private List<Authority> authorities = new ArrayList<>();

    public void setIdForTest(Long id) {
        this.id = id;
    }
}
