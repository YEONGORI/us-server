package us.usserver.comment.novel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import us.usserver.author.Author;
import us.usserver.base.BaseEntity;
import us.usserver.novel.Novel;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "novel_comment_id")
    private Long id;

    @NotBlank
    @Length(max = 300)
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "novel_id")
    private Novel novel;
}
