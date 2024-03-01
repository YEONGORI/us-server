package us.usserver.domain.chapter.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import us.usserver.global.BaseEntity;
import us.usserver.domain.chapter.constant.ChapterStatus;
import us.usserver.domain.comment.entity.Comment;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.paragraph.entity.Paragraph;

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
