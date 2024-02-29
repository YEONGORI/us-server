package us.usserver.domain.comment.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.comment.dto.CommentContent;
import us.usserver.domain.comment.dto.CommentInfo;
import us.usserver.domain.comment.dto.GetCommentResponse;

@Service
public interface CommentService {
    GetCommentResponse getCommentsOfNovel(Long novelId);
    GetCommentResponse getCommentsOfChapter(Long chapterId);

    CommentInfo writeCommentOnNovel(Long novelId, Long authorId, CommentContent commentContent);
    CommentInfo writeCommentOnChapter(Long chapterId, Long authorId, CommentContent commentContent);

    GetCommentResponse getCommentsByAuthor(Long authorId);

    void deleteComment(Long commentId, Long authorId);
}
