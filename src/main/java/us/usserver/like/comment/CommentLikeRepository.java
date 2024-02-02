package us.usserver.like.comment;

import org.springframework.stereotype.Repository;
import us.usserver.comment.Comment;

@Repository
public interface CommentLikeRepository {
    CommentLike save(CommentLike commentLike);

    void delete(CommentLike commentLike);

    CommentLike findByComment(Comment comment);
}
