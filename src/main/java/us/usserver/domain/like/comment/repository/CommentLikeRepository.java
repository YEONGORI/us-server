package us.usserver.domain.like.comment.repository;

import org.springframework.stereotype.Repository;
import us.usserver.domain.comment.Comment;
import us.usserver.domain.like.comment.CommentLike;

@Repository
public interface CommentLikeRepository {
    CommentLike save(CommentLike commentLike);

    void delete(CommentLike commentLike);

    CommentLike findByComment(Comment comment);
}
