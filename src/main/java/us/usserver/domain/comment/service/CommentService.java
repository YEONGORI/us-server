package us.usserver.domain.comment.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.comment.dto.CommentContent;
import us.usserver.domain.comment.dto.CommentInfo;
import us.usserver.domain.comment.dto.GetCommentResponse;

@Service
public interface CommentService {
    GetCommentResponse getCommentsOfNovel(Long novelId);
    GetCommentResponse getCommentsOfChapter(Long chapterId);

    CommentInfo writeCommentOnNovel(Long novelId, Long memberId, CommentContent commentContent);
    CommentInfo writeCommentOnChapter(Long chapterId, Long memberId, CommentContent commentContent);

    GetCommentResponse getCommentsByAuthor(Long memberId);

    void deleteComment(Long commentId, Long memberId);
}
