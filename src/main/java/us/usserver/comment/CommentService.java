package us.usserver.comment;

import org.springframework.stereotype.Service;
import us.usserver.comment.dto.CommentContent;
import us.usserver.comment.dto.CommentInfo;
import us.usserver.comment.dto.GetCommentResponse;

import java.util.List;

@Service
public interface CommentService {
    GetCommentResponse getCommentsOfNovel(Long novelId);
    GetCommentResponse getCommentsOfChapter(Long chapterId);

    CommentInfo writeCommentOnNovel(Long novelId, Long authorId, CommentContent commentContent);
    CommentInfo writeCommentOnChapter(Long chapterId, Long authorId, CommentContent commentContent);

    GetCommentResponse getCommentsByAuthor(Long authorId);

    void deleteComment(Long commentId, Long authorId);
}
