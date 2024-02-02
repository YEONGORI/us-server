package us.usserver.paragraph;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import us.usserver.author.Author;
import us.usserver.base.BaseEntity;
import us.usserver.chapter.Chapter;
import us.usserver.like.paragraph.ParagraphLike;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paragraph extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paragraph_id")
    private Long id;

    @NotBlank
    @Size(max = 300, min = 50)
    private String content;

    @Min(0)
    private int sequence;

    @NotNull
    private ParagraphStatus paragraphStatus;

    @ManyToOne
    @JoinColumn
    private Chapter chapter;

    @ManyToOne
    @JoinColumn
    private Author author;

    @OneToMany(mappedBy = "paragraph", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParagraphLike> paragraphLikes = new ArrayList<>();

    public void setSequenceForTest(int sequence) {
        this.sequence = sequence;
    }

    public void setParagraphStatusForTest(ParagraphStatus paragraphStatus) {
        this.paragraphStatus = paragraphStatus;
    }
}
