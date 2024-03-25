package us.usserver.domain.comment.service;

import org.springframework.stereotype.Service;
import us.usserver.domain.comment.dto.CommentContent;
import us.usserver.domain.comment.dto.CommentInfo;
import us.usserver.domain.comment.dto.GetCommentRes;

@Service
public interface CommentService {
    GetCommentRes getCommentsOfNovel(Long novelId);
    GetCommentRes getCommentsOfChapter(Long chapterId);

    CommentInfo writeCommentOnNovel(Long novelId, Long memberId, CommentContent commentContent);
    CommentInfo writeCommentOnChapter(Long chapterId, Long memberId, CommentContent commentContent);

    GetCommentRes getCommentsByAuthor(Long memberId);

    void deleteComment(Long commentId, Long memberId);
}
