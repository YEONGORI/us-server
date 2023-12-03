package us.usserver.like.comment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.author.Author;
import us.usserver.comment.chapter.ChComment;

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

    @ManyToOne
    @JoinColumn(name = "chapter_comment_id")
    private ChComment chComment;
}
