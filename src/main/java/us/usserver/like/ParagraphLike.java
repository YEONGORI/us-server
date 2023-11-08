package us.usserver.like;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.author.Author;
import us.usserver.paragraph.Paragraph;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paragraphLike_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn
    private Paragraph paragraph;
}
