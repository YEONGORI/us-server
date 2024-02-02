package us.usserver.like.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import us.usserver.comment.Comment;
import us.usserver.like.comment.CommentLike;

import java.util.Optional;

public interface CommentLikeJpaRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByComment(Comment comment);

}
