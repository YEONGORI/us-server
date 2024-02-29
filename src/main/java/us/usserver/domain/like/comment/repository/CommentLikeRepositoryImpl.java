package us.usserver.domain.like.comment.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import us.usserver.domain.comment.Comment;
import us.usserver.domain.like.comment.CommentLike;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeRepository {
    private final CommentLikeJpaRepository commentLikeJpaRepository;

    @Override
    public CommentLike save(CommentLike commentLike) {
        return commentLikeJpaRepository.save(commentLike);
    }

    @Override
    public void delete(CommentLike commentLike) {
        commentLikeJpaRepository.delete(commentLike);
    }

    @Override
    public CommentLike findByComment(Comment comment) {
        Optional<CommentLike> byComment = commentLikeJpaRepository.findByComment(comment);
        return byComment.orElse(null);
    }
}
