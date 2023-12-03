package us.usserver.comment.novel;

import org.springframework.stereotype.Service;
import us.usserver.comment.novel.dto.CommentsInNovelRes;
import us.usserver.comment.novel.dto.PostCommentReq;

import java.util.List;

@Service
public interface NoCommentService {
    List<CommentsInNovelRes> getCommentsInNovel(Long novelId);

    List<CommentsInNovelRes> postCommentInNovel(Long novelId, Long authorId, PostCommentReq postCommentReq);
}
