package us.usserver.comment;

import org.springframework.stereotype.Service;
import us.usserver.comment.dto.CommentContent;
import us.usserver.comment.dto.CommentInfo;

import java.util.List;

@Service
public interface CommentService {
    List<CommentInfo> getCommentsOfNovel(Long novelId);
    List<CommentInfo> getCommentsOfChapter(Long chapterId);

    CommentInfo writeCommentOnNovel(Long novelId, Long authorId, CommentContent commentContent);
    CommentInfo writeCommentOnChapter(Long chapterId, Long authorId, CommentContent commentContent);

    List<CommentInfo> getCommentsByAuthor(Long authorId);

    void deleteComment(Long commentId, Long authorId);
}
