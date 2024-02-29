package us.usserver.domain.like.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import us.usserver.domain.comment.Comment;
import us.usserver.domain.like.comment.CommentLike;

import java.util.Optional;

public interface CommentLikeJpaRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByComment(Comment comment);

}
