package us.usserver.like.comment;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.comment.chapter.ChComment;
import us.usserver.like.Like;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChCommentLike extends Like {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "chapter_comment_like_id")
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "author_id")
//    private Author author;

    @ManyToOne
    @JoinColumn(name = "chapter_comment_id")
    private ChComment chComment;
}
