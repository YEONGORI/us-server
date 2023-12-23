package us.usserver.author;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import us.usserver.authority.Authority;
import us.usserver.comment.chapter.ChComment;
import us.usserver.comment.novel.NoComment;
import us.usserver.like.comment.ChCommentLike;
import us.usserver.like.novel.NovelLike;
import us.usserver.like.paragraph.ParagraphLike;
import us.usserver.member.Member;
import us.usserver.novel.Novel;
import us.usserver.paragraph.Paragraph;
import us.usserver.score.Score;
import us.usserver.stake.Stake;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long id;

    @NotBlank
    @Size(max = 11)
    private String nickname;

    @Size(max = 100)
    private String introduction;

    //프로필 사진을 설정 하지 않았을 때 default 이미지 값을 Input 예정
    @Size(max = 500)
    private String profileImg;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //TODO[고민]: author에 비중이 너무 커져서 읽은 소설 같은 경우에는 member에 추가를 하면 안될까..?
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Novel> readNovels = new ArrayList<>();

    @OneToMany(mappedBy = "mainAuthor", cascade = CascadeType.ALL)
    private List<Novel> createNovels = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Paragraph> paragraphs = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Score> scores = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Stake> stakes = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Authority> authorities = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<NovelLike> novelLikes = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<ParagraphLike> paragraphLikes = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<NoComment> noComments = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<ChComment> chComments = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<ChCommentLike> chCommentLikeList = new ArrayList<>();

    public void addAuthorNovel(Authority authority) {
        authority.setAuthor(this);
        this.authorities.add(authority);
    }
}
