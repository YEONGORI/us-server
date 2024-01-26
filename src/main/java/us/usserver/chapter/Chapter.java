package us.usserver.chapter;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import us.usserver.base.BaseEntity;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.comment.Comment;
import us.usserver.novel.Novel;
import us.usserver.paragraph.Paragraph;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chapter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chapter_id")
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotNull
    private Integer part;

    @Setter
    private ChapterStatus status;

    @ManyToOne
    @JoinColumn
    private Novel novel;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private List<Paragraph> paragraphs = new ArrayList<>();

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public void setStatusForTest(ChapterStatus status) {
        this.status = status;
    }
    public void setPartForTest(Integer part) {
        this.part = part;
    }
}
