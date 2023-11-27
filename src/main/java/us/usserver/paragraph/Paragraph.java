package us.usserver.paragraph;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import us.usserver.author.Author;
import us.usserver.base.BaseEntity;
import us.usserver.chapter.Chapter;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;

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
    @Size(max = 300)
    private String content;

    @Min(0)
    @Max(15)
    private int sequence;

    @NotNull
    @Setter
    private ParagraphStatus paragraphStatus;

    @ManyToOne
    @JoinColumn
    private Chapter chapter;

    @ManyToOne
    @JoinColumn
    private Author author;
}
