package us.usserver.score;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.author.Author;
import us.usserver.base.BaseEntity;
import us.usserver.chapter.Chapter;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Score extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @Max(10)
    @Min(1)
    @NotNull
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn
    private Chapter chapter;
}
