package us.usserver.paragraph;


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
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paragraph extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 300)
    private String content;

    @NotNull
    private ParagraphStatus paragraphStatus;

    @ManyToOne
    @JoinColumn
    private Chapter chapter;
}
