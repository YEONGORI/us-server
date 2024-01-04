package us.usserver.like.comment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.author.Author;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chapter_comment_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
