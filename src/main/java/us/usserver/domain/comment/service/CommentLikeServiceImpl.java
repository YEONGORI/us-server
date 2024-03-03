package us.usserver.domain.comment.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.comment.entity.Comment;
import us.usserver.domain.comment.entity.CommentLike;
import us.usserver.domain.comment.repository.CommentLikeRepository;
import us.usserver.global.EntityFacade;
import us.usserver.global.response.exception.BaseException;
import us.usserver.global.response.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {
    private final EntityFacade entityFacade;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public void postLike(Long commentId, Long authorId) {
        Author author = entityFacade.getAuthor(authorId);
        Comment comment = entityFacade.getComment(commentId);

        CommentLike commentLike = CommentLike.builder().comment(comment).author(author).build();
        comment.getCommentLikes().add(commentLike);
    }

    @Override
    public void deleteLike(Long commentId, Long authorId) {
        Author author = entityFacade.getAuthor(authorId);
        Comment comment = entityFacade.getComment(commentId);

        CommentLike commentLike = commentLikeRepository.findByComment(comment)
                .orElseThrow(() -> new BaseException(ErrorCode.COMMENT_NOT_FOUND));

        if (!Objects.equals(commentLike.getAuthor().getId(), author.getId())) {
            throw new BaseException(ErrorCode.AUTHOR_NOT_AUTHORIZED);
        }
        comment.getCommentLikes().remove(commentLike); // EqualsAndHashCode 재정의 완료
    }
}
