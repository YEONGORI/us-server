package us.usserver.domain.paragraph.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.domain.member.entity.Author;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParagraphLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paragraph_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "paragraph_id")
    private Paragraph paragraph;
}
