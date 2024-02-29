package us.usserver.domain.like.comment;

import jakarta.persistence.*;
import lombok.*;
import us.usserver.domain.member.entity.Author;
import us.usserver.domain.comment.Comment;

@Entity
@Getter
@Builder
@EqualsAndHashCode // remove 때 equals 비교를 위해 사용
@NoArgsConstructor
@AllArgsConstructor
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
