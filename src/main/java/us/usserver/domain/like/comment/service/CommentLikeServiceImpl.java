package us.usserver.domain.like.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.usserver.domain.member.entity.Author;
import us.usserver.domain.comment.Comment;
import us.usserver.domain.like.comment.repository.CommentLikeRepository;
import us.usserver.global.EntityService;
import us.usserver.global.ExceptionMessage;
import us.usserver.global.exception.AuthorNotAuthorizedException;
import us.usserver.domain.like.comment.CommentLike;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {
    private final EntityService entityService;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public void postLike(Long commentId, Long authorId) {
        Author author = entityService.getAuthor(authorId);
        Comment comment = entityService.getComment(commentId);

        CommentLike commentLike = CommentLike.builder().comment(comment).author(author).build();
        comment.getCommentLikes().add(commentLike);
        // commentLikeRepository.save(commentLike);
    }

    @Override
    public void deleteLike(Long commentId, Long authorId) {
        Author author = entityService.getAuthor(authorId);
        Comment comment = entityService.getComment(commentId);

        CommentLike commentLike = commentLikeRepository.findByComment(comment);
        if (!Objects.equals(commentLike.getAuthor().getId(), author.getId())) {
            throw new AuthorNotAuthorizedException(ExceptionMessage.AUTHOR_NOT_AUTHORIZED);
        }
        comment.getCommentLikes().remove(commentLike); // EqualsAndHashCode 재정의 완료
        // commentLikeRepository.delete(commentLike);
    }
}
